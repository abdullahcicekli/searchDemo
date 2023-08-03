package com.example.searchTool.searchDemo.service;

import com.example.searchTool.searchDemo.model.Document;
import com.example.searchTool.searchDemo.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.*;

@Service
public class IndexService {
    private final Map<String, List<List<Long>>> invertedIndex;
    private final Set<String> stopWords;
    private final DocumentRepository documentRepository;

    @Autowired
    public IndexService(DocumentRepository documentRepository) {
        this.invertedIndex = new HashMap<>();
        this.stopWords = createStopWords();
        this.documentRepository = documentRepository;
    }

    public void indexDocument(Document document) {
        long documentId = saveDocument(document);
        addDocumentToIndex(document, documentId);
    }

    public void bulkIndexDocument(List<Document> documents) {
        for (Document document : documents) {
            indexDocument(document);
        }
    }

    private long saveDocument(Document document) {
        Document savedDocument = documentRepository.save(document);
        return savedDocument.getId();
    }

    private void addDocumentToIndex(Document document, long documentId) {
        String content = document.getContent();
        BreakIterator wordIterator = BreakIterator.getWordInstance();
        wordIterator.setText(content);

        List<String> words = new ArrayList<>();
        int start = wordIterator.first();
        int end = wordIterator.next();
        while (end != BreakIterator.DONE) {
            String word = content.substring(start, end);
            String cleanedWord = cleanWord(word);

            if (!isStopWord(cleanedWord)) {
                words.add(cleanedWord);
            }

            start = end;
            end = wordIterator.next();
        }

        int wordCount = words.size();
        for (int i = 0; i < wordCount; i++) {
            String word = words.get(i);
            for (int j = 3; j < word.length() + 1; j++) {
                String subWord = word.substring(0, j);
                List<List<Long>> occurrences = invertedIndex.computeIfAbsent(subWord, k -> new ArrayList<>());
                List<Long> occurrence = Arrays.asList(documentId, (long) i);

                if (!occurrences.contains(occurrence)) {
                    occurrences.add(occurrence);
                }
            }
        }
        System.out.println(invertedIndex);
    }

    private String cleanWord(String word) {
        String cleanedWord = word.replaceAll("[^\\p{L}\\p{N}]", "").toLowerCase().trim();
        return cleanedWord;
    }

    private boolean isStopWord(String word) {
        return stopWords.contains(word);
    }

    private Set<String> createStopWords() {
        Set<String> stopWords = new HashSet<>();
        stopWords.add("ve");
        stopWords.add("de");
        stopWords.add("ile");
        stopWords.add("");
        return stopWords;
    }

    public Map<String, List<List<Long>>> getInvertedIndex() {
        return invertedIndex;
    }
}
