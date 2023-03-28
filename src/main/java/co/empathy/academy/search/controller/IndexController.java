package co.empathy.academy.search.controller;

import co.empathy.academy.search.service.IndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.utils.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;
    @PostMapping("")
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
            @RequestParam("basic") MultipartFile basicMultipartFile,
            @RequestParam("aka") MultipartFile akaMultipartFile,
            @RequestParam("episode") MultipartFile episodeMultipartFile,
            @RequestParam("principal") MultipartFile principalMultipartFile,
            @RequestParam("rating") MultipartFile ratingMultipartFile,
            @RequestParam("crew") MultipartFile crewMultipartFile
    ) throws IOException {
        System.out.println("Indexing started");
        File basic = this._getTempFile(basicMultipartFile);
        File aka = this._getTempFile(akaMultipartFile);
        File episode = this._getTempFile(episodeMultipartFile);
        File principal = this._getTempFile(principalMultipartFile);
        File rating = this._getTempFile(ratingMultipartFile);
        File crew = this._getTempFile(crewMultipartFile);
        System.out.println("Files created");
        JobId jobId = BackgroundJob.enqueue(() ->
                indexService.index(basic, aka, episode, principal, rating, crew)
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    private File _getTempFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", ".tmp");
        OutputStream os = new FileOutputStream(tempFile);
        IOUtils.copyStream(file.getInputStream(), os);
        tempFile.deleteOnExit();
        return tempFile;
    }
}
