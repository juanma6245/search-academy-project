package co.empathy.academy.search.service;

import co.empathy.academy.search.common.TSVtype;
import co.empathy.academy.search.model.title.Title;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface TSVService {
    List<Title> readTSV(File file, TSVtype type) throws IOException;
    Title readOneTSV(BufferedReader input, TSVtype type) throws IOException;
}
