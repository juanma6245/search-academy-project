package co.empathy.academy.search.service;

import co.empathy.academy.search.repository.ElasticConnection;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    private ElasticConnection connection;


    @Override
    public String search(String query) throws ParseException, IOException {
        String response;
        if (!query.isBlank()) {
            //response = this.connection.executeQuery(query);
            response = this.connection.getClusterName();
        } else {
            response = "Error: Query is blank";
        }
        return response;
    }

    @Override
    public String getClusterName() throws ParseException, IOException {
        return this.connection.getClusterName();
    }
}
