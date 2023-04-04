package co.empathy.academy.search.controller;

import co.empathy.academy.search.service.IndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.utils.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.jobrunr.scheduling.JobBuilder.aJob;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;
    @PostMapping("{indexName}")
    @Operation(summary = "Index the data",
            description = "Index the data",
            tags = {"Index"},
            operationId = "index",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Indexing started"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity index(
            @PathVariable("indexName") String indexName,
            @RequestParam("basic") MultipartFile basicMultipartFile,
            @RequestParam("aka") MultipartFile akaMultipartFile,
            @RequestParam("episode") MultipartFile episodeMultipartFile,
            @RequestParam("principal") MultipartFile principalMultipartFile,
            @RequestParam("rating") MultipartFile ratingMultipartFile,
            @RequestParam("crew") MultipartFile crewMultipartFile,
            //@RequestParam("config") MultipartFile configMultipartFile,
            @RequestParam("mapping") MultipartFile mappingMultipartFile
    ) throws IOException {
        System.out.println("Indexing started");
        File basic = this._getTempFile(basicMultipartFile);
        File aka = this._getTempFile(akaMultipartFile);
        File episode = this._getTempFile(episodeMultipartFile);
        File principal = this._getTempFile(principalMultipartFile);
        File rating = this._getTempFile(ratingMultipartFile);
        File crew = this._getTempFile(crewMultipartFile);
        //File config = this._getTempFile(configMultipartFile);
        File mapping = this._getTempFile(mappingMultipartFile);
        System.out.println("Files created");
        //Create index and set configuration and mappings
        this.indexService.createIndex(indexName);
        //this.indexService.setConfiguration(indexName, config);
        this.indexService.setMapping(indexName, mapping);
        //System.out.println("Index created and set configuration and mappings");
        JobId indexJob = BackgroundJob.create(aJob()
                .withName("Start indexing")
                .withDetails(() -> this.indexService.index(indexName, basic, aka, episode, principal, rating, crew)));

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("{indexName}")
    @Operation(summary = "Delete the index",
            description = "Delete the index",
            tags = {"Index"},
            operationId = "deleteIndex",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Index deleted"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity deleteIndex(@PathVariable String indexName) throws IOException {
        JsonObject response = null;
        HttpStatus status = HttpStatus.OK;
        if (this.indexService.deleteIndex(indexName)) {
            response = Json.createObjectBuilder()
                    .add("message", "Index deleted")
                    .build();
        } else {
            response = Json.createObjectBuilder()
                    .add("message", "Index not found")
                    .build();
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity(response, status);
    }

    /**
     * Convert a MultipartFile to a temporary file
     * @param file File to be converted to a temporary file
     * @return a temporary file
     * @throws IOException if the file cannot be created
     */
    private File _getTempFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", ".tmp");
        OutputStream os = new FileOutputStream(tempFile);
        IOUtils.copyStream(file.getInputStream(), os);
        tempFile.deleteOnExit();
        return tempFile;
    }

}
