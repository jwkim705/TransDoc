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
        Map<String, Object> properties = new HashMap<>();

        // Calc to PDF 변환 옵션
        properties.put("FilterData", getFilterData());
        properties.put("FilterOptions", "PrintAllSheets");

        return LocalConverter.builder()
                .officeManager(officeManager)
                .storeProperties(properties)
                .formatRegistry(registry)
                .build();
    }

    private Map<String, Object> getFilterData() {
        Map<String, Object> filterData = new HashMap<>();

        // 내보내기 옵션
        Map<String, Object> exportOptions = new HashMap<>();
        exportOptions.put("SinglePageSheets", true);  // 각 시트를 별도 페이지로 설정
        exportOptions.put("ExportNotes", false);

        filterData.put("CalcExportSettings", exportOptions);
        return filterData;
    }
}