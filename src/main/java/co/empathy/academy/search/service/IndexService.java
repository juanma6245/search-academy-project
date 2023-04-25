package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.indices.IndexState;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Service to index the files into elasticsearch
 */
public interface IndexService {

    /**
     * Indexes the files into elasticsearch in a bulk request with the given index name. If the index does not exist, it will be created.
     * The index will be created with the default configuration and mapping if it doesn't exist.
     * The bulk request will be split in chunks of MAX_LINES documents.
     * @param indexName name of the index
     * @param basic    basics file
     * @param aka     akas file
     * @param episode episodes file
     * @param principal principals file
     * @param rating ratings file
     * @param crew   crew file
     * @throws IOException if there is an error when indexing into elasticsearch
     */
    void index(String indexName,File basic, File aka, File episode, File principal, File rating, File crew) throws IOException;
    /**
     * Creates an index with the given name.
     * @param indexName Name of the index to be created.
     * @return true if the index was created, false if there is an error with elasticsearch.
     * @throws IOException if there is an error when creating the index.
     */
    boolean createIndex(String indexName) throws IOException;
    /**
     * Deletes an index with the given name.
     * @param indexName Name of the index to be deleted.
     * @return true if the index was deleted, false if index can not be deleted.
     * @throws IOException if there is an error when deleting the index.
     */
    boolean deleteIndex(String indexName) throws IOException;
    /**
     * Sets the configuration of the index with the given name.
     * @param indexName Name of the index of which the configuration will be set.
     * @param configFile File containing the configuration. The file must be in json format.
     * @throws IOException if there is an error when setting the configuration.
     */
    void setConfig(String indexName,File configFile) throws IOException;
    /**
     * Sets the mapping of the index with the given name.
     * @param indexName Name of the index of which the mapping will be set.
     * @param mappingFile File containing the mapping. The file must be in json format.
     * @throws IOException if there is an error when setting the mapping.
     */
    void setMapping(String indexName,File mappingFile) throws IOException;
    /**
     * Checks if the index with the given name exists.
     * @param indexName Name of the index to be checked.
     * @return true if the index exists, false otherwise.
     * @throws IOException if there is an error when checking if the index exists.
     */
    boolean indexExists(String indexName) throws IOException;

    Map<String, IndexState> getIndexes() throws IOException;

    /**
     * Indexes the names into elasticsearch in a bulk request with the given index name. If the index does not exist, it will be created.
     * @param indexName name of the index
     * @param namesFile file containing the names to be indexed
     * @throws IOException if there is an error when indexing into elasticsearch
     */
    void indexNames(String indexName, File namesFile) throws IOException;
}
