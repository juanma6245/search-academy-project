package co.empathy.academy.search.common;

import co.empathy.academy.search.common.factory.FactoryTitle;
import co.empathy.academy.search.model.title.*;
import co.empathy.academy.search.service.TSVService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to read the titles from the TSV files. It uses the TSVService to read the files
 */
public class TitleReader {


    private TSVService tsvService;

    public TitleReader(TSVService tsvService) {
        this.tsvService = tsvService;
    }

    /**
     * Method to read a Basic title from a file
     * @param reader BufferedReader to read the file
     * @return Basic object with the basic information of the title
     * @throws IOException if the file is not found
     */
    public Basic getBasic(BufferedReader reader) throws IOException {
        return (Basic) tsvService.readOneTSV(reader, TSVtype.BASIC);
    }

    /**
     * Method to read a list of titles from a file (the method uses the tcost of the title to read the file).
     * Mostly used for AKAS and PRINCIPALS
     * @param reader BufferedReader to read the file
     * @param basic Basic object with the tcost of the title
     * @param type TSVtype to read the file
     * @return List of Title objects with the information of the title
     * @throws IOException if the file is not found
     */
    public List<Title> getList(BufferedReader reader, Basic basic, TSVtype type) throws IOException {
        List<Title> response = new ArrayList<>();
        String tconst = basic.getTconst();
        boolean continueNextId = false;
        while (!continueNextId) {
            reader.mark(10000);
            Title title = tsvService.readOneTSV(reader, type);
            if (title == null) {
                continueNextId = true;
            } else if (title.getId().equals(tconst)) {
                //System.out.println("Title: " + tconst);
                response.add(title);
            } else {
                continueNextId = true;
            }
        }
        reader.reset();
        return response;
    }

    /**
     * @param reader BufferedReader to read the file
     * @param basic Basic object with the tcost of the title
     * @return Rating object with the rating information of the title
     * @throws IOException if the file is not found
     */
    public Rating getRating(BufferedReader reader, Basic basic) throws IOException {
        reader.mark(10000);
        Rating rating = (Rating) tsvService.readOneTSV(reader, TSVtype.RATING);
        FactoryTitle factory = FactoryTitle.getInstance();
        if (rating.getTconst().equals(basic.getTconst())) {
            return rating;
        } else {
            reader.reset();
            return (Rating) factory.getTitle(TSVtype.RATING, new String[]{basic.getTconst(), "0.0", "0"});
        }
    }

    /**
     * @param cr BufferedReader to read the file
     * @return Crew object with the crew information of the title
     * @throws IOException if the file is not found
     */
    public Crew getCrew(BufferedReader cr) throws IOException {
        return (Crew) tsvService.readOneTSV(cr, TSVtype.CREW);
    }

    /**
     * This method is used to read from the name.basics.tsv file.
     * @param nr BufferedReader to read the file
     * @return String array with tha names information
     * @throws IOException
     */
    public String[] getName(BufferedReader nr) throws IOException {
        String line = nr.readLine();
        if (line == null) {
            return null;
        }
        return line.split("\t");
    }
}
