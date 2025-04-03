package com.rsupport.TransDoc.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
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

    private final DocumentConverter documentConverter;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> convertToPdf(@RequestParam("file") MultipartFile file)
        throws IOException, OfficeException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            log.info("convert start");

            documentConverter.convert(file.getInputStream()).to(baos).as(DefaultDocumentFormatRegistry.PDF).execute();

            log.info("convert end");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=converted.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());
        }
    }

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<FileSystemResource> convertToPdfCommend(@RequestParam("file") MultipartFile file) {
//
//        try {
//        File tempInput = File.createTempFile("input_",".tmp", new File("/Users/jaewon/Downloads/jodconverter-profile-template"));
//        file.transferTo(tempInput);
//
////        File convertedPdf = new File("/Users/jaewon/Downloads/jodconverter-profile-template", tempInput.getName().replace(".tmp", ".pdf"));
//
//            ProcessBuilder processBuilder = new ProcessBuilder(
//                    "/Applications/LibreOffice.app/Contents/MacOS/soffice",
//                    "--headless",
//                    "--convert-to",
//                    "pdf",
//                    "--outdir",
//                    "/Users/jaewon/Downloads/jodconverter-profile-template",
//                    tempInput.getAbsolutePath()
//            );
//            processBuilder.redirectErrorStream(true);
//
//            Process process = processBuilder.start();
//            int exitCode = process.waitFor();
//            if (exitCode != 0) {
//                throw new RuntimeException("변환 중 오류 발생");
//            }
//
//            // 변환된 PDF 파일을 클라이언트에게 전송
//            FileSystemResource resource = new FileSystemResource(tempInput.getAbsolutePath());
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tempInput.getName());
//
//            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}