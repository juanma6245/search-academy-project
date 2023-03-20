package co.empathy.academy.search.service;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.common.factory.FactoryTitle;
import co.empathy.academy.search.common.factory.IFactoryTitle;
import co.empathy.academy.search.model.title.Title;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class TSVServiceImpl implements TSVService {

    @Override
    public List<Title> readTSV(File file, CSVtype type) throws IOException {
        List<Title> response = new ArrayList<>();
        IFactoryTitle factory = FactoryTitle.getInstance();
        BufferedReader br;

        String line;
        InputStream input = new FileInputStream(file);
        br = new BufferedReader(new InputStreamReader(input));
        String headers = br.readLine();
        while ((line = br.readLine()) != null) {
            //System.out.println(line);
            String[] data = line.split("\t");

            Title title = factory.getTitle(type, data);
            if (title != null) {
                //System.out.println(title);
                response.add(title);
            }
        }
        System.out.println("Total: " + response.size());
        br.close();
        return response;
    }
}
