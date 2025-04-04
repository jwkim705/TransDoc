package com.rsupport.TransDoc.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
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

        byte[] pdfContent = convertFileToPdfBytes(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=converted.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @PostMapping(path = "/path", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> convertToPdfResPath(@RequestParam("file") MultipartFile file)
        throws IOException, OfficeException {


        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        File outputFile = saveConvertedFile(file, uniqueFileName);

        return ResponseEntity.ok(outputFile.getAbsolutePath());
    }

    @PostMapping(path = "/path/multi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> convertMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException, OfficeException {
        List<String> convertedFilePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
            File outputFile = saveConvertedFile(file, uniqueFileName);
            convertedFilePaths.add(outputFile.getAbsolutePath());
        }

        return ResponseEntity.ok(convertedFilePaths);
    }

    /**
     * 파일을 PDF 바이트 배열로 변환
     */
    private byte[] convertFileToPdfBytes(MultipartFile file) throws IOException, OfficeException {
        DocumentConverter converter = createDocumentConverter();
        log.info("PDF 변환 시작: {}", file.getOriginalFilename());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            converter.convert(file.getInputStream())
                    .to(outputStream)
                    .as(DefaultDocumentFormatRegistry.PDF)
                    .execute();

            log.info("PDF 변환 완료: {}", file.getOriginalFilename());
            return outputStream.toByteArray();
        }
    }

    /**
     * 파일을 PDF로 변환하여 디스크에 저장
     */
    private File saveConvertedFile(MultipartFile file, String outputFileName)
            throws IOException, OfficeException {
        Path datePath = getDateBasedPath();
        File outputFile = new File(datePath.toString(), outputFileName);

        DocumentConverter converter = createDocumentConverter();
        log.info("PDF 변환 및 저장 시작: {}", file.getOriginalFilename());

        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            converter.convert(file.getInputStream())
                    .to(outputStream)
                    .as(DefaultDocumentFormatRegistry.PDF)
                    .execute();

            log.info("PDF 변환 및 저장 완료: {}", outputFileName);
            return outputFile;
        } catch (Exception e) {
            log.error("파일 변환 중 오류 발생: {}", file.getOriginalFilename(), e);
            throw e;
        }
    }

    /**
     * 날짜별 경로 생성
     */
    private Path getDateBasedPath() throws IOException {
        String datePathStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path datePath = Paths.get(workingDir, datePathStr);

        if (Files.notExists(datePath)) {
            Files.createDirectories(datePath);
        }

        return datePath;
    }

    /**
     * 문서 변환기 생성
     */
    private DocumentConverter createDocumentConverter() {
        return LocalConverter.make(officeManager);
    }

    /**
     * 디렉토리가 존재하는지 확인하고, 없으면 생성
     */
    private void ensureDirectoryExists(File directory) throws IOException {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("디렉토리 생성 실패: " + directory.getAbsolutePath());
        }
    }

    /**
     * 고유한 파일 이름 생성
     */
    private String generateUniqueFileName(String originalFilename) {
        String baseName = originalFilename != null ?
                FilenameUtils.getBaseName(originalFilename) : "converted";
        return baseName + "_" + System.currentTimeMillis() + ".pdf";
    }

}