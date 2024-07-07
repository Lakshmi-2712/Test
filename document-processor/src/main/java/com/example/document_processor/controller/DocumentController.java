package com.example.document_processor.controller;

import com.example.document_processor.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    private final OpenAIService openAIService;

    @Autowired
    public DocumentController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
                                 @RequestParam("fields") String fields, Model model) {
        try {
            if (file.isEmpty()) {
                model.addAttribute("result", "Please select a file to upload");
                return "upload";
            }

            String documentContent;
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.endsWith(".pdf")) {
                documentContent = convertPDFToText(file);
            } else {
                documentContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            }

            logger.info("Document content length: {}", documentContent.length());

            // Split document into smaller, manageable chunks
            List<String> chunks = splitDocumentContent(documentContent);
            StringBuilder results = new StringBuilder();

            for (String chunk : chunks) {
                String extractedField = openAIService.extractField(chunk, fields);
                results.append(extractedField).append("\n");
            }

            model.addAttribute("result", results.toString().trim());

            return "upload";
        } catch (Exception e) {
            logger.error("Error processing document", e);
            model.addAttribute("result", "Error processing document: " + e.getMessage());
            return "upload";
        }
    }

    private List<String> splitDocumentContent(String documentContent) {
        int chunkSize = 16000;  // Adjust this value if needed
        List<String> chunks = new ArrayList<>();
        int start = 0;

        while (start < documentContent.length()) {
            int end = Math.min(documentContent.length(), start + chunkSize);
            chunks.add(documentContent.substring(start, end));
            start = end;
        }

        return chunks;
    }

    private String convertPDFToText(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}
