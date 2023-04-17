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
import jakarta.json.*;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.processing.Filer;
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
                                 @RequestParam(value = "minMinutes", required = false) String minMinutesString,
                                 @RequestParam(value = "genres", required = false) String[] genres) throws IOException, NoSearchResultException, ParseException {
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter(Filter.TYPE.TERM,"titleType", titleTypeString));
        if (startYearString != null) {
            filters.add(new Filter(Filter.TYPE.RANGE, "startYear", startYearString));
        }
        if (minMinutesString != null) {
            filters.add(new Filter(Filter.TYPE.RANGE,"runtimeMinutes", minMinutesString));
        }
        if (genres != null) {
            for (String genre : genres) {
                filters.add(new Filter(Filter.TYPE.TERM,"genres", genre));
            }
        }
        SearchResponse<ResponseDocument> result = this.searchService.search(INDEX_NAME, query, filters);
        List<ResponseDocument> documents = new ArrayList<>();
        for (Hit<ResponseDocument> hit : result.hits().hits()) {
            documents.add(hit.source());
        }
        List<Map<String, Object>> aggs = new ArrayList<>();
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
}
