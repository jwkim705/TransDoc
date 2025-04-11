package com.rsupport.TransDoc.config;

import org.jodconverter.core.document.DocumentFamily;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.document.SimpleDocumentFormatRegistry;

public class CustomDocumentFormatRegistry extends SimpleDocumentFormatRegistry {

    public CustomDocumentFormatRegistry() {
        super();

        // HWP 등록
        DocumentFormat hwp = DocumentFormat.builder()
                .name("HWP")
                .extension("hwp")
                .mediaType("application/x-hwp")
                .inputFamily(DocumentFamily.TEXT)
                .build();

        // HWPX 등록
        DocumentFormat hwpx = DocumentFormat.builder()
                .name("HWPX")
                .extension("hwpx")
                .mediaType("application/x-hwpml")
                .inputFamily(DocumentFamily.TEXT)
                .build();

        // PPT 형식 등록 (PowerPoint 97-2003)
        DocumentFormat ppt = DocumentFormat.builder()
            .name("Microsoft PowerPoint")
            .extension("ppt")
            .mediaType("application/vnd.ms-powerpoint")
            .inputFamily(DocumentFamily.PRESENTATION)
            .build();

        // PPTX 형식 등록 (PowerPoint 2007+)
        DocumentFormat pptx = DocumentFormat.builder()
            .name("Microsoft PowerPoint Open XML")
            .extension("pptx")
            .mediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation")
            .inputFamily(DocumentFamily.PRESENTATION)
            .build();

        addFormat(hwp);
        addFormat(hwpx);
        addFormat(ppt);
        addFormat(pptx);
    }
}