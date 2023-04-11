package co.empathy.academy.search.common;

import co.empathy.academy.search.common.factory.FactoryTitle;
import co.empathy.academy.search.common.factory.IFactoryTitle;
import co.empathy.academy.search.model.title.Basic;
import co.empathy.academy.search.model.title.Title;
import co.empathy.academy.search.service.TSVService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TitleReaderTest {

    private TSVService tsvService;
    private TitleReader titleReader;

    private Title testBasic;
    private Title testRating;
    private Title testCrew;

    @BeforeEach
    void setUp() throws IOException {
        IFactoryTitle factoryTitle = FactoryTitle.getInstance();
        String[] basicData = new String[]{"tt0000001", "short", "Carmencita", "Carmencita", "0", "1894", "\\N", "1", "Documentary,Short"};
        String[] ratingData = new String[]{"tt0000001", "5.7", "1959"};
        String[] crewData = new String[]{"tt0000001", "nm0005690", "\\N"};
        testBasic = factoryTitle.getTitle(TSVtype.BASIC, basicData);
        testRating = factoryTitle.getTitle(TSVtype.RATING, ratingData);
        testCrew = factoryTitle.getTitle(TSVtype.CREW, crewData);
        tsvService = mock();
        titleReader = new TitleReader(tsvService);
        given(tsvService.readOneTSV(any(), eq(TSVtype.BASIC))).willReturn(testBasic);
        given(tsvService.readOneTSV(any(), eq(TSVtype.RATING))).willReturn(testRating);
        given(tsvService.readOneTSV(any(), eq(TSVtype.CREW))).willReturn(testCrew);
        given(tsvService.readOneTSV(any(), eq(TSVtype.AKA))).willReturn(null);
    }

    @Test
    void getBasic() throws IOException {
        assertEquals(testBasic, titleReader.getBasic(null));
    }

    @Test
    void getList() throws IOException {
        BufferedReader reader = mock();
        List<Title> result = titleReader.getList(reader, (Basic) testBasic, TSVtype.AKA);
        assertEquals(0, result.size());
    }

    @Test
    void getRating() throws IOException {
        BufferedReader reader = mock();
        assertEquals(testRating, titleReader.getRating(reader, (Basic) testBasic));
    }

    @Test
    void getCrew() throws IOException {
        assertEquals(testCrew, titleReader.getCrew(null));
    }
}