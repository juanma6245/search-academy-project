package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.indices.IndexState;
import co.empathy.academy.search.common.DocumentStorage;
import co.empathy.academy.search.common.TitleReader;
import co.empathy.academy.search.model.title.*;
import jakarta.json.Json;
import co.empathy.academy.search.common.TSVtype;
import co.empathy.academy.search.repository.ElasticConnection;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jobrunr.scheduling.JobBuilder.aJob;

@Service
public class IndexServiceImpl implements IndexService{

    @Autowired
    private TSVService TSVService;
    @Autowired
    private ElasticConnection elasticConnection;
    @Autowired
    private DocumentStorage documentStorage;

    //private static final String INDEX_NAME = "imdb";
    private static final int MAX_LINES = 50000;
    private BufferedReader br;
    private BufferedReader ar;
    private BufferedReader er;
    private BufferedReader pr;
    private BufferedReader rr;
    private BufferedReader cr;
    private BufferedReader nr;
    private boolean endFile;
    private TitleReader reader;


    @Override
    public void index(String indexName, File basic, File aka, File episode, File principal, File rating, File crew) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(new FileInputStream(basic)));
        this.ar = new BufferedReader(new InputStreamReader(new FileInputStream(aka)));
        this.er = new BufferedReader(new InputStreamReader(new FileInputStream(episode)));
        this.pr = new BufferedReader(new InputStreamReader(new FileInputStream(principal)));
        this.rr = new BufferedReader(new InputStreamReader(new FileInputStream(rating)));
        this.cr = new BufferedReader(new InputStreamReader(new FileInputStream(crew)));
        this.endFile = false;
        this.reader = new TitleReader(TSVService);
        while(this.endFile == false) {
            //long start = System.currentTimeMillis();
            List<JsonObject> json = this._getData();
            String key = this.documentStorage.add(json);
            //long end = System.currentTimeMillis();
            //System.out.println("Time read: " + (end - start) + "ms");
            JobId jobId = BackgroundJob.create(aJob()
                    .withName("Indexing data")
                    .withDetails(() -> elasticConnection.bulk(indexName, key)));

            //elasticConnection.bulk(INDEX_NAME, json);

        }

    }

    @Override
    public boolean createIndex(String indexName) throws IOException {
        if (this.elasticConnection.indexExists(indexName)){
            this.elasticConnection.deleteIndex(indexName);
        }
        return this.elasticConnection.createIndex(indexName); //Trows exception when index already exists
    }

    @Override
    public boolean deleteIndex(String indexName) throws IOException {
        return this.elasticConnection.deleteIndex(indexName); //Trows exception when index not exists
    }

    @Override
    public void setConfig(String indexName, File configFile) throws IOException {
        this.elasticConnection.setConfig(indexName, configFile);
    }

    @Override
    public void setMapping(String indexName, File mappingFile) throws IOException {
        this.elasticConnection.setMapping(indexName, mappingFile);
    }

    @Override
    public boolean indexExists(String indexName) throws IOException {
        return this.elasticConnection.indexExists(indexName);
    }
    
    @Override
    public Map<String, IndexState> getIndexes() throws IOException {
        Map<String, IndexState> indices = this.elasticConnection.getClient().indices().get(request -> request.index("*")).result();
        return indices;
    }

    @Override
    public void indexNames(String indexName, File namesFile) throws IOException {
        this.reader = new TitleReader(TSVService);
        this.nr = new BufferedReader(new InputStreamReader(new FileInputStream(namesFile)));
        this.endFile = false;
        while(this.endFile == false) {
            List<JsonObject> json = this._getNames();
            String key = this.documentStorage.add(json);
            JobId jobId = BackgroundJob.create(aJob()
                    .withName("Indexing data Names")
                    .withDetails(() -> elasticConnection.bulk(indexName, key)));
        }
    }

    /**
     * Generates a list of json objects with the names read from the nr reader and a maximum length of MAX_LINES
     * @return List of json objects with the names read from the nr reader
     * @throws IOException
     */
    private List<JsonObject> _getNames() throws IOException {
        int linesRead = 0;
        List<JsonObject> response = new ArrayList<>();

        while (linesRead < MAX_LINES) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            //Read name data
            String[] nameData = reader.getName(this.nr);
            if (nameData == null) {
                this.endFile = true;
                break;
            }
            //Build the json
            this._buildFromData(nameData, builder);
            response.add(builder.build());
            linesRead++;
        }
        return response;
    }

    /**
     * Adds nconst and primaryName to the builder. The rest of the data is not needed
     * @param nameData data read from the reader
     * @param builder json builder
     */
    private void _buildFromData(String[] nameData, JsonObjectBuilder builder) {
        builder.add("nconst", nameData[0]);
        builder.add("primaryName", nameData[1]);
        //Just need name
    }

    /**
     * Generates a list of json objects with the data read from the readers and a maximum of MAX_LINES
     * @return List of json objects with the data read from the readers
     * @throws IOException if there is an error reading from readers
     */
    private List<JsonObject> _getData() throws IOException {
        int linesRead = 0;
        List<JsonObject> response = new ArrayList<>();

        while (linesRead < MAX_LINES) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            //Read basic data
            Basic basicData = reader.getBasic(this.br);
            //if(basicData.getPrimaryTitle() == "episode")
            if (basicData == null) {
                this.endFile = true;
                break;
            }
            //Read rating data
            Rating ratingData = reader.getRating(this.rr, basicData);
            //Read aka data
            List<Title> akaData = reader.getList(this.ar, basicData, TSVtype.AKA);
            //Read Crew data to get directors
            Crew crew = reader.getCrew(this.cr);
            //Read principal data to get actors
            List<Title> principalData = reader.getList(this.pr, basicData, TSVtype.PRINCIPAL);
            if (!basicData.isAdult()) {
                //Build the json
                this._buildFromData(basicData, ratingData, akaData, crew, principalData, builder);
                response.add(builder.build());
            }
            linesRead++;
        }

        return response;
    }

    /**
     * Builds a json object into the builder with the data from the parameters
     * @param basicData Basic data of the title
     * @param ratingData Rating data of the title
     * @param akaData Aka data of the title
     * @param crew Crew data of the title
     * @param principalData Principal data of the title
     * @param builder Builder to build the json object
     */
    private void _buildFromData(Basic basicData, Rating ratingData, List<Title> akaData, Crew crew, List<Title> principalData, JsonObjectBuilder builder) {
        builder.add("id", basicData.getTconst());
        builder.add("tconst", basicData.getTconst());
        builder.add("titleType", basicData.getTitleType());
        builder.add("primaryTitle", basicData.getPrimaryTitle());
        builder.add("originalTitle", basicData.getOriginalTitle());
        builder.add("isAdult", basicData.isAdult());
        builder.add("startYear", basicData.getStartYear());
        builder.add("endYear", basicData.getEndYear());
        builder.add("runtimeMinutes", basicData.getRuntimeMinutes());
        JsonArrayBuilder genres = Json.createArrayBuilder();
        for (String genre : basicData.getGenres()) {
            genres.add(genre);
        }
        builder.add("genres", genres.build());
        builder.add("averageRating", ratingData.getAverageRating());
        builder.add("numVotes", ratingData.getNumVotes());
        JsonArrayBuilder akas = Json.createArrayBuilder();
        for (Title title : akaData) {
            Aka aka = (Aka) title;
            JsonObjectBuilder akaBuilder = Json.createObjectBuilder();
            akaBuilder.add("title", aka.getTitle());
            akaBuilder.add("region", aka.getRegion());
            akaBuilder.add("language", aka.getLanguage());
            akaBuilder.add("isOriginalTitle", aka.isOriginalTitle());
            akas.add(akaBuilder.build());
        }
        builder.add("akas", akas.build());
        JsonArrayBuilder directors = Json.createArrayBuilder();
        for (String director : crew.getDirectors()) {
            JsonObjectBuilder directorBuilder = Json.createObjectBuilder();
            directorBuilder.add("nconst", director);
            directors.add(directorBuilder.build());
        }
        builder.add("directors", directors.build());
        JsonArrayBuilder actors = Json.createArrayBuilder();
        for (Title title : principalData) {
            Principal principal = (Principal) title;
            JsonObjectBuilder actorBuilder = Json.createObjectBuilder();
            JsonObjectBuilder nameBuilder = Json.createObjectBuilder();
            nameBuilder.add("nconst", principal.getNconst());
            actorBuilder.add("name", nameBuilder.build());
            actorBuilder.add("characters", principal.getCharacters());
            actors.add(actorBuilder.build());
        }
        builder.add("starring", actors.build());
    }
}
