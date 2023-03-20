package co.empathy.academy.search.service;

import co.empathy.academy.search.common.CSVtype;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileTaskService {
    void uploadFileTSV(File file, CSVtype type) throws InterruptedException, IOException;
}
