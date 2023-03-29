package co.empathy.academy.search.service;

import java.io.File;
import java.io.IOException;

public interface IndexService {
    void index(String indexName,File basic, File aka, File episode, File principal, File rating, File crew) throws IOException;

    boolean createIndex(String indexName) throws IOException;
    boolean deleteIndex(String indexName) throws IOException;
    void setConfig(String indexName,File configFile) throws IOException;
    void setMapping(String indexName,File mappingFile) throws IOException;
    boolean indexExists(String indexName) throws IOException;

}
