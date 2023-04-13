package co.empathy.academy.search.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exception.NoSearchResultException;
import co.empathy.academy.search.model.ResponseDocument;
import co.empathy.academy.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class SearchController {
    @Autowired
    private SearchService searchService;
    private static final String INDEX_NAME = "imdb";

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
    public ResponseEntity search(@RequestParam("query") String query) throws IOException, NoSearchResultException {
        List<Hit<ResponseDocument>> list = this.searchService.search(INDEX_NAME, query);
        List<ResponseDocument> response = new ArrayList<>();
        for (Hit<ResponseDocument> hit : list) {
            response.add(hit.source());
        }
        return ResponseEntity.ok(response);
    }
}
