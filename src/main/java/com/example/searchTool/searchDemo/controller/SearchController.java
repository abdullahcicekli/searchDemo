package com.example.searchTool.searchDemo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.searchTool.searchDemo.model.Document;
import com.example.searchTool.searchDemo.service.IndexService;
import com.example.searchTool.searchDemo.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final IndexService indexService;
    private final SearchService searchService;

    @PostMapping
    public void indexData(@RequestBody Document document) {
        indexService.indexDocument(document);
    }

    @PostMapping("/bulk")
    public void bulkIndex(@RequestBody List<Document> documentList) {
        indexService.bulkIndexDocument(documentList);
    }

    @GetMapping
    public List<String> searchData(@RequestParam String query) {
        return searchService.performSearch(query);
    }
}