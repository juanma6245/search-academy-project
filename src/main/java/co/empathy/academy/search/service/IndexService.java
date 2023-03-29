package co.empathy.academy.search.service;

import java.io.File;
import java.io.IOException;

public interface IndexService {
    void index(File basic, File aka, File episode, File principal, File rating, File crew) throws IOException;

    boolean createIndex(String indexName) throws IOException;
    boolean deleteIndex(String indexName) throws IOException;
    void setConfig(File configFile);
    boolean isIndexExists(String indexName);

}
