package co.empathy.academy.search.service;

import co.empathy.academy.search.common.TitleReader;
import co.empathy.academy.search.model.title.*;
import jakarta.json.Json;
import co.empathy.academy.search.common.TSVtype;
import co.empathy.academy.search.repository.ElasticConnection;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.jobrunr.scheduling.JobBuilder.aJob;

@Service
public class IndexServiceImpl implements IndexService{

    @Autowired
    private TSVService TSVService;
    @Autowired
    private ElasticConnection elasticConnection;

    private static final String INDEX_NAME = "imdb";
    private static final int MAX_LINES = 1000;
    private BufferedReader br;
    private BufferedReader ar;
    private BufferedReader er;
    private BufferedReader pr;
    private BufferedReader rr;
    private BufferedReader cr;
    private boolean endFile;
    private TitleReader reader;


    @Override
    public void index(File basic, File aka, File episode, File principal, File rating, File crew) throws IOException {
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
            List<String> json = this._getData();
            //long end = System.currentTimeMillis();
            //System.out.println("Time read: " + (end - start) + "ms");
            JobId jobId = BackgroundJob.create(aJob()
                    .withName("Indexing data")
                    .withDetails(() -> elasticConnection.bulk(INDEX_NAME, json)));

            //elasticConnection.bulk(INDEX_NAME, json);

        }

    }

    @Override
    public boolean createIndex(String indexName) throws IOException {
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

    private List<String> _getData() throws IOException {
        int linesRead = 0;
        List<String> response = new ArrayList<>();

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
            //Build the json
            this._buildFromData(basicData, ratingData, akaData, crew, principalData, builder);
            response.add(builder.build().toString());
            linesRead++;
        }

        return response;
    }

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
