package com.example.searchTool.searchDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.searchTool.searchDemo.model.Document;
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
