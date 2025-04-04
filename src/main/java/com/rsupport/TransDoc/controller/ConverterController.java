package com.rsupport.TransDoc.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/convert")
@RequiredArgsConstructor
@Slf4j
public class ConverterController {

    private final OfficeManager officeManager;

    @Value("${jodconverter.local.working-dir}")
    private String workingDir;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> convertToPdf(@RequestParam("file") MultipartFile file)
        throws IOException, OfficeException {

        DocumentConverter converter = LocalConverter.make(officeManager);

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            log.info("convert start");

            converter.convert(file.getInputStream()).to(baos).as(DefaultDocumentFormatRegistry.PDF).execute();

            log.info("convert end");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=converted.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());
        }
    }

    @PostMapping(path = "/path", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> convertToPdfResPath(@RequestParam("file") MultipartFile file)
        throws IOException, OfficeException {


        String outputFileName = "converted_" + System.currentTimeMillis() + ".pdf"; // Unique file name
        File outputFile = new File(workingDir, outputFileName);

        if (!outputFile.getParentFile().exists()) {
            boolean dirsCreated = outputFile.getParentFile().mkdirs();
            if (!dirsCreated) {
                throw new IOException("Failed to create output directory: " + workingDir);
            }
        }

        DocumentConverter converter = LocalConverter.make(officeManager);

        try(OutputStream os = new FileOutputStream(outputFile)){
            log.info("convert start");

            converter.convert(file.getInputStream()).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();

            log.info("convert end");

            String filePath = outputFile.getAbsolutePath();
                    return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            log.error("Error during file conversion", e);
            throw e; // Re-throw exception for handling
        }
    }
}