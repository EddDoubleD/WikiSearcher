package com.edddoubled.wikisearcher.controller;


import com.edddoubled.wikisearcher.model.LightweightWikiPage;
import com.edddoubled.wikisearcher.service.search.Searcher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {

    Searcher searcher;

    @GetMapping
    public ResponseEntity<List<LightweightWikiPage>> search(@RequestParam String query) {
        return ResponseEntity.ok().body(searcher.search(query));
    }
}
