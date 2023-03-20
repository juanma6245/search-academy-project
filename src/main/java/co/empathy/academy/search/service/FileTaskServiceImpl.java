package co.empathy.academy.search.service;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.model.title.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileTaskServiceImpl implements FileTaskService{

        @Autowired
        private TSVService TSVService;
        @Override
        public void uploadFileTSV(File file, CSVtype type) throws InterruptedException, IOException {
            //Handle file
            List<Title> titles = TSVService.readTSV(file, type);
            //Save in ElasticSearch
        }
}
