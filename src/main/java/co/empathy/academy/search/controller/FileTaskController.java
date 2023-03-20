package co.empathy.academy.search.controller;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.service.FileTaskService;
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

@RestController
@RequestMapping("/file")
public class FileTaskController {

    @Autowired
    private FileTaskService fileTaskService;

    @PostMapping("")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws IOException {
        File tmp = this.getTempFile(file);
        JobId jobId = BackgroundJob.enqueue(() ->
                fileTaskService.uploadFileTSV(tmp, CSVtype.valueOf(type.toUpperCase()))
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    private File getTempFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", ".tmp");
        OutputStream os = new FileOutputStream(tempFile);
        IOUtils.copyStream(file.getInputStream(), os);

        return tempFile;
    }
}
