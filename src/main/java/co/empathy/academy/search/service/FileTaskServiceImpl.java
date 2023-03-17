package co.empathy.academy.search.service;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.model.title.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileTaskServiceImpl implements FileTaskService{

        @Autowired
        private CSVService csvService;
        @Override
        public void uploadFileTSV(MultipartFile file, CSVtype type) throws InterruptedException {
            Thread.sleep(10000);
            //Handle file
            List<Title> titles = csvService.readCSV(file, type);
            //Save in ElasticSearch
        }
}
