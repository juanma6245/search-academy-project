package co.empathy.academy.search.service;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.model.title.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Service
public class FileTaskServiceImpl implements FileTaskService{

        @Autowired
        private TSVService TSVService;
        @Override
        public void uploadFileTSV(File file, CSVtype type) throws InterruptedException, IOException {
            //Handle file
            //List<Title> titles = TSVService.readTSV(file, type);
                long lines = Files.lines(file.toPath()).toArray().length - 1;
                InputStream input = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String headers = br.readLine();
                for (int i = 0; i < lines; i++) {
                    Title title = TSVService.readOneTSV(br, type);
                    System.out.println(title);
                    //Save in ElasticSearch
                }
        }
}
