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
//        Map<String, Object> filterData = new HashMap<>();
//        filterData.put("PageFitToPages", 1);

//        Map<String, Object> singlePageSheetProps = new HashMap<>();
//        singlePageSheetProps.put("type", "boolean");
//        singlePageSheetProps.put("value", true);
//        filterData.put("SinglePageSheets", singlePageSheetProps);

        Map<String, Object> storeProperties = new HashMap<>();
        storeProperties.put("FilterName", "calc_pdf_Export"); // Calc PDF 내보내기 필터 지정
        storeProperties.put("FilterData", Map.of(
            "Selection", "all",
            "FitToPages", true,
            "ScaleToPagesX", 1,
            "ScaleToPagesY", 1,
            "ReduceImageResolution", true,
            "MaxImageResolution", 300,
            "PageFitToPages", 1
        ));

        return LocalConverter.builder()
                .officeManager(officeManager)
                .storeProperties(storeProperties)
                .formatRegistry(registry)
                .build();
    }
}