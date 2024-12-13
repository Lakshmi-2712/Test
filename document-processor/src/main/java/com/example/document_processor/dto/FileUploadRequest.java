package com.example.document_processor.dto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public class FileUploadRequest {
	private MultipartFile file;
    private List<String> fields;

    // Getters and setters
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}

