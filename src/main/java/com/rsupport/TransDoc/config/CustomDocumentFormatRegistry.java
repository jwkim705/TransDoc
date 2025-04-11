package com.rsupport.TransDoc.config;

import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFamily;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.document.DocumentFormatRegistry;
import org.jodconverter.core.document.SimpleDocumentFormatRegistry;

public class CustomDocumentFormatRegistry extends SimpleDocumentFormatRegistry {

    public CustomDocumentFormatRegistry() {
        super();

        DocumentFormatRegistry defaultRegistry = DefaultDocumentFormatRegistry.getInstance();

        for (DocumentFamily family : DocumentFamily.values()) {
            for (DocumentFormat format : defaultRegistry.getOutputFormats(family)) {
                addFormat(format);
            }
        }

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

        addFormat(hwp);
        addFormat(hwpx);
    }
}