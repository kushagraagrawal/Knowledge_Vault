package com.stackroute.controller;


import com.stackroute.domain.DocumentReader;
import com.stackroute.domain.OutputForDoc;
import com.stackroute.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {
    @Qualifier("DocumentServiceImpl")
    private DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    @PostMapping()
    public ResponseEntity<?> saveDocuments(@RequestBody List<DocumentReader> documentReaderList){
        ResponseEntity responseEntity;

        List<DocumentReader> documentReaderList1 = new ArrayList<>();
        try {
            documentReaderList1.add(new DocumentReader(1, "title", new String(Files.readAllBytes(Paths.get("Files/document1.txt")))));
            documentReaderList1.add(new DocumentReader(2, "title", new String(Files.readAllBytes(Paths.get("Files/document2.txt")))));
            documentReaderList1.add(new DocumentReader(3, "title", new String(Files.readAllBytes(Paths.get("Files/document3.txt")))));
            documentReaderList1.add(new DocumentReader(4, "title", new String(Files.readAllBytes(Paths.get("Files/document4.txt")))));
            documentReaderList1.add(new DocumentReader(5, "title", new String(Files.readAllBytes(Paths.get("Files/document5.txt")))));
        }catch (IOException e){
            e.printStackTrace();
        }

        List<DocumentReader> documentReaders = documentService.saveDocuments(documentReaderList1);
        responseEntity = new ResponseEntity<List<DocumentReader>>(documentReaders, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping()
    public ResponseEntity<?> getAllTerms(){
        ResponseEntity responseEntity;
        List<DocumentReader> documentReaderList = documentService.getAllDocuments();
        List<OutputForDoc> outputForDocList = documentService.processDoc(documentReaderList);
        responseEntity = new ResponseEntity<List<OutputForDoc>>(outputForDocList, HttpStatus.OK);
        return responseEntity;
    }
}
