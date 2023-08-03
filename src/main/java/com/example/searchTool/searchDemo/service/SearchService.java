package com.example.searchTool.searchDemo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.searchTool.searchDemo.model.Document;
import com.example.searchTool.searchDemo.repository.DocumentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService
{
    private final IndexService indexService;
    private final DocumentRepository documentRepository;
    public List<String> performSearch(String keyword) {
        List<String> searchResults = new ArrayList<>();

        Map<String, List<List<Long>>> invertedIndex = indexService.getInvertedIndex();

        List<List<Long>> occurrences = invertedIndex.getOrDefault(keyword, Collections.emptyList());

        for (List<Long> occurrence : occurrences) {
            Long documentId = occurrence.get(0);
            Integer wordIndex = occurrence.get(1).intValue();
            Document document = documentRepository.findById(documentId).orElse(null);
            if (document != null) {
                String content = document.getContent();
                String[] words = content.split("\\s+");
                if (wordIndex < words.length) {
                    String result = "Found in document " + documentId + ", word: " + words[wordIndex];
                    searchResults.add(result);
                }
            }
        }

        return searchResults;
    }

}
