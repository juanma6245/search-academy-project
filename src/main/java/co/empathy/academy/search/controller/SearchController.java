package co.empathy.academy.search.controller;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exception.NoSearchResultException;
import co.empathy.academy.search.model.Filter;
import co.empathy.academy.search.model.ResponseDocument;
import co.empathy.academy.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class SearchController {
    @Autowired
    private SearchService searchService;
    private static final String INDEX_NAME = "test";

    @Operation(summary = "Search for documents in elasticSearch that match the query",
            tags = {"Search"},
            operationId = "searchByQuery",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content =  @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDocument.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @GetMapping("")
    public ResponseEntity search(@RequestParam("query") String query,
                                 @RequestParam(value = "titleType", required = false, defaultValue = "movie") String titleTypeString,
                                 @RequestParam(value = "startYear", required = false) String startYearString,
                                 @RequestParam(value = "endYear", required = false) String endYearString,
                                 @RequestParam(value = "minMinutes", required = false) String minMinutesString,
                                 @RequestParam(value = "maxMinutes", required = false) String maxMinutesString,
                                 @RequestParam(value = "minRating", required = false) String minRatingString,
                                 @RequestParam(value = "maxRating", required = false) String maxRatingString,
                                 @RequestParam(value = "minVotes", required = false) String minVotesString,
                                 @RequestParam(value = "genres", required = false) String[] genres,
                                 @RequestParam(value = "size", required = false, defaultValue = "100") int numDocs,
                                 @RequestParam(value = "page", defaultValue = "0") int page) throws IOException, NoSearchResultException, ParseException {

        //Add filters
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter(Filter.TYPE.TERM,"titleType", titleTypeString));
        if (startYearString != null) {
            filters.add(new Filter(Filter.TYPE.MIN, "startYear", startYearString));
        }
        if (endYearString != null) {
            filters.add(new Filter(Filter.TYPE.MAX, "startYear", endYearString));
        }
        if (minMinutesString != null) {
            filters.add(new Filter(Filter.TYPE.MIN,"runtimeMinutes", minMinutesString));
        }
        if (maxMinutesString != null) {
            filters.add(new Filter(Filter.TYPE.MAX,"runtimeMinutes", maxMinutesString));
        }
        if (minRatingString != null) {
            filters.add(new Filter(Filter.TYPE.MIN,"averageRating", minRatingString));
        }
        if (maxRatingString != null) {
            filters.add(new Filter(Filter.TYPE.MAX,"averageRating", maxRatingString));
        }
        if (minVotesString != null) {
            filters.add(new Filter(Filter.TYPE.MIN,"numVotes", minVotesString));
        }
        if (genres != null) {
            for (String genre : genres) {
                filters.add(new Filter(Filter.TYPE.TERM,"genres", genre));
            }
        }
        //Search request
        SearchResponse<ResponseDocument> result = this.searchService.search(INDEX_NAME, query, numDocs, page, filters);
        //Parse response
        List<ResponseDocument> documents = new ArrayList<>();
        for (Hit<ResponseDocument> hit : result.hits().hits()) {
            documents.add(hit.source());
        }

        List<Map<String, Object>> aggs = new ArrayList<>();
        //Parse aggregations
        Map<String, Aggregate> aggsMap = result.aggregations();
        for (Map.Entry<String, Aggregate> entry: aggsMap.entrySet()) {
            Object value = null;
            if (entry.getValue().isMin()){
                value = entry.getValue().min().value();
            } else if (entry.getValue().isMax()) {
                value = entry.getValue().max().value();
            } else if (entry.getValue().isSterms()) {
                value = new ArrayList<>();
                for (var bucket : entry.getValue().sterms().buckets().array()) {
                    Map<String, Object> bucketMap = Map.of(
                            "key", bucket.key().stringValue()
                            ,"docCount", bucket.docCount()
                    );
                    ((List) value).add(bucketMap);
                }
            } else {
                value = entry.getValue();
            }
            Map<String, Object> aggMap = Map.of(
                    "name", entry.getKey()
                    ,"value", value
            );
            aggs.add(aggMap);
        }
        Map<String, Object> response = Map.of(
                "total", documents.size()
                ,"hits", documents
                ,"aggs", aggs
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get trending films in elasticSearch, trending is defined by the rating of the title",
            tags = {"Search"},
            operationId = "trending",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content =  @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDocument.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @GetMapping("/trending")
    public ResponseEntity trending(@RequestParam(value = "size", required = false, defaultValue = "20") int numDocs,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "minNumVotes", required = false, defaultValue = "100") String minNumVotes,
                                   @RequestParam(value = "titleType", required = false, defaultValue = "movie") String titleTypeString) throws IOException, NoSearchResultException, ParseException {
        //Add filters
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter(Filter.TYPE.TERM,"titleType", titleTypeString));
        if (minNumVotes != null) {
            filters.add(new Filter(Filter.TYPE.MIN, "numVotes", minNumVotes));
        }
        //Search request
        SearchResponse<ResponseDocument> result = this.searchService.trending(INDEX_NAME, numDocs, page, filters);
        //Parse response
        List<ResponseDocument> documents = new ArrayList<>();
        for (Hit<ResponseDocument> hit : result.hits().hits()) {
            documents.add(hit.source());
        }

        Map<String, Object> response = Map.of(
                "total", documents.size()
                ,"hits", documents
        );

        return ResponseEntity.ok(response);
    }
}
