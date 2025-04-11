package com.rsupport.TransDoc.config;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DocumentConfig {

    @Bean
    @Primary
    public CustomDocumentFormatRegistry customDocumentFormatRegistry() {
        return new CustomDocumentFormatRegistry();
    }

    @Bean
    @Primary
    public DocumentConverter documentConverter(OfficeManager officeManager,
                                               CustomDocumentFormatRegistry registry) {
        return LocalConverter.builder()
                .officeManager(officeManager)
                .formatRegistry(registry)
                .build();
    }
}