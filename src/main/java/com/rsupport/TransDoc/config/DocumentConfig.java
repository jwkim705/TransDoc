package com.rsupport.TransDoc.config;

import java.util.HashMap;
import java.util.Map;
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
        Map<String, Object> storeProperties = new HashMap<>();
        storeProperties.put("FilterName", "calc_pdf_Export");
        Map<String, Object> filterData = new HashMap<>();
        filterData.put("SinglePageSheets", true);
//        filterData.put("OpenInFullScreenMode", true);
        storeProperties.put("FilterData", filterData);

        return LocalConverter.builder()
                .officeManager(officeManager)
                .storeProperties(storeProperties)
                .formatRegistry(registry)
                .build();
    }

}