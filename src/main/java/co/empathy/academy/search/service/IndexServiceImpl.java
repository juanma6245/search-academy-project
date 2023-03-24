package co.empathy.academy.search.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class IndexServiceImpl implements IndexService{
    @Override
    public void index(File basic, File aka, File episode, File principal, File rating, File crew) {

    }

    @Override
    public void createIndex(String indexName) {

    }

    @Override
    public boolean deleteIndex(String indexName) {
        return false;
    }

    @Override
    public void setConfig(File configFile) {

    }

    @Override
    public boolean isIndexExists(String indexName) {
        return false;
    }
}
