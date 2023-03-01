package co.empathy.academy.search.service;

import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;

public interface SearchService {
    String search(String query) throws ParseException, IOException;
    String getClusterName() throws ParseException, IOException;
}
