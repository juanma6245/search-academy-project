package co.empathy.academy.search.service;

import co.empathy.academy.search.common.CSVtype;
import org.springframework.web.multipart.MultipartFile;

public interface FileTaskService {
    void uploadFileTSV(MultipartFile file, CSVtype type) throws InterruptedException;
}
