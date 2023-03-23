package co.empathy.academy.search.service;

import co.empathy.academy.search.common.TSVtype;

import java.io.File;
import java.io.IOException;

public interface FileTaskService {
    void uploadFileTSV(File file, TSVtype type) throws InterruptedException, IOException;
}
