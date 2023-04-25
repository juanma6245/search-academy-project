package co.empathy.academy.search.service;

import co.empathy.academy.search.common.TSVtype;
import co.empathy.academy.search.model.title.Title;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface TSVService {
    /**
     * Method to read a list of titles from a file. Deprecated because files are too big to read them all at once.
     * @param file File to read
     * @param type TSVtype to read the file
     * @return List of Title objects with the information of the title
     * @throws IOException if the file is not found
     */
    List<Title> readTSV(File file, TSVtype type) throws IOException;

    /**
     * Method to read a title from a file
     * @param input BufferedReader to read the file
     * @param type TSVtype to read the file
     * @return Title object with the information of the title
     * @throws IOException if the file is not found
     */
    Title readOneTSV(BufferedReader input, TSVtype type) throws IOException;
}
