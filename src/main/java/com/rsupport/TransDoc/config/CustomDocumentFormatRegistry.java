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
        addFormat(hwp);

        // HWPX 등록
        DocumentFormat hwpx = DocumentFormat.builder()
                .name("HWPX")
                .extension("hwpx")
                .mediaType("application/x-hwpml")
                .inputFamily(DocumentFamily.TEXT)
                .build();
        addFormat(hwpx);
    }
}