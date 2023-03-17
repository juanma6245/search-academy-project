package co.empathy.academy.search.service;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.model.title.Title;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVService {
    List<Title> readCSV(MultipartFile file, CSVtype type);
}
