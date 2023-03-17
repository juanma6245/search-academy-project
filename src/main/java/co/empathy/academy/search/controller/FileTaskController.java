package co.empathy.academy.search.controller;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.service.FileTaskService;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileTaskController {

    @Autowired
    private FileTaskService fileTaskService;

    @PostMapping("")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) {
        JobId jobId = BackgroundJob.enqueue(() ->
                fileTaskService.uploadFileTSV(file, CSVtype.valueOf(type.toUpperCase()))
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
