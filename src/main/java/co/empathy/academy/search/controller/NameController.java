package co.empathy.academy.search.controller;

import co.empathy.academy.search.exception.NoSearchResultException;
import co.empathy.academy.search.model.ResponseName;
import co.empathy.academy.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/name")
public class NameController {
    private static final String INDEX_NAME = "names";
    @Autowired
    private SearchService searchService;

    @Operation(summary = "Search for a name that match the given nconst",
            tags = {"Name"},
            operationId = "searchName",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping("")
    public ResponseEntity search(@RequestParam("nconst") String nconst) throws NoSearchResultException, IOException {
        ResponseName nameSearchResponse = this.searchService.searchName(INDEX_NAME, nconst);

        return ResponseEntity.ok(nameSearchResponse);
    }
}
