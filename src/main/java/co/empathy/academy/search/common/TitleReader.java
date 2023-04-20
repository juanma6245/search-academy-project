package co.empathy.academy.search.common;

import co.empathy.academy.search.common.factory.FactoryTitle;
import co.empathy.academy.search.model.title.*;
import co.empathy.academy.search.service.TSVService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TitleReader {


    private TSVService tsvService;

    public TitleReader(TSVService tsvService) {
        this.tsvService = tsvService;
    }

    public Basic getBasic(BufferedReader reader) throws IOException {
        return (Basic) tsvService.readOneTSV(reader, TSVtype.BASIC);
    }

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

    public Crew getCrew(BufferedReader cr) throws IOException {
        return (Crew) tsvService.readOneTSV(cr, TSVtype.CREW);
    }

    public String[] getName(BufferedReader nr) throws IOException {
        String line = nr.readLine();
        if (line == null) {
            return null;
        }
        return line.split("\t");
    }
}
