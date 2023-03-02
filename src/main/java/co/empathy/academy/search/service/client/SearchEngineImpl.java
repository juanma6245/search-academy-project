package co.empathy.academy.search.service.client;


import co.empathy.academy.search.repository.ElasticConnection;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class SearchEngineImpl implements SearchEngine{

    @Autowired
    private ElasticConnection connection;



    @Override
    public String executeQuery(String query) throws ParseException, IOException {
        return connection.getClusterName();
        //return connection.executeQuery(query);
    }
}
