package co.empathy.academy.search.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IndexService {
    void index(File basic, File aka, File episode, File principal, File rating, File crew) throws IOException;

    void createIndex(String indexName);
    boolean deleteIndex(String indexName);
    void setConfig(File configFile);
    boolean isIndexExists(String indexName);

}
