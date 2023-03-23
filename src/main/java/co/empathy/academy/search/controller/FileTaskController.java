package co.empathy.academy.search.controller;

import co.empathy.academy.search.common.TSVtype;
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
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/file")
public class FileTaskController {

    //private static final int numLines = 10000;
    @Autowired
    private FileTaskService fileTaskService;

    @PostMapping("")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws IOException {
        File tmp = this._getTempFile(file);
        JobId jobId = BackgroundJob.enqueue(() ->
                //this.handleBigFile(tmp, TSVtype.valueOf(type.toUpperCase()))
                fileTaskService.uploadFileTSV(tmp, TSVtype.valueOf(type.toUpperCase()))
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
    /*
    public void handleBigFile(File file, TSVtype type) throws IOException {
        List<File> files = this._divideFile(file, type, numLines);
        for (File f : files) {
            JobId jobId = BackgroundJob.enqueue(() ->
                    fileTaskService.uploadFileTSV(f, type)
            );
        }
    }*/

    /*
    private List<File> _divideFile(File file,TSVtype type, int numLines) throws IOException {
        long lines = Files.lines(file.toPath()).toArray().length;
        long numFiles = (int) Math.ceil((double) lines / numLines);
        Scanner scanner = new Scanner(file);
        List<File> files = new LinkedList<>();
        for (int i = 0; i < numFiles; i++) {
            File tmp = File.createTempFile("temp", ".tmp");
            int remainingLines = numLines;
            while (remainingLines > 0 && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Files.write(tmp.toPath(), line.getBytes());
                remainingLines--;
            }
            tmp.deleteOnExit();
            files.add(tmp);
            //JobId jobId = BackgroundJob.enqueue(() ->
            //        fileTaskService.uploadFileTSV(tmp, type)
            //);
            System.out.println("File " + i + " created");
        }

        return files;
    }*/
}
