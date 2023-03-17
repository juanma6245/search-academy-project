package co.empathy.academy.search.service;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.model.title.Title;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class CSVServiceImpl implements CSVService{

    @Override
    public List<Title> readCSV(MultipartFile file, CSVtype type) {
        return null;
    }
}
