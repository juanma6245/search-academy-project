package co.empathy.academy.search.service.client;

import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;

public interface SearchEngine {
    String executeQuery(String query) throws ParseException, IOException;

}
