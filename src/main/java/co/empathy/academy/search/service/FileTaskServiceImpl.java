package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.empathy.academy.search.common.TSVtype;
import co.empathy.academy.search.model.title.Title;
import co.empathy.academy.search.repository.ElasticConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

@Service
public class FileTaskServiceImpl implements FileTaskService{

    @Autowired
    private TSVService TSVService;
    @Autowired
    private ElasticConnection elasticConnection;
    @Override
    public void uploadFileTSV(File file, TSVtype type) throws IOException {
        //Handle file
        //List<Title> titles = TSVService.readTSV(file, type);
        long lines = Files.lines(file.toPath()).toArray().length;
        InputStream input = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        //String headers = br.readLine();
        IndexResponse response;
        for (int i = 0; i < lines; i++) {
            Title title = TSVService.readOneTSV(br, type);
            response = elasticConnection.index(title, type);
            //System.out.println(response.id());
        }
        file.delete();
    }
}
