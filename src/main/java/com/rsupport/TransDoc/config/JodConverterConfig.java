package com.rsupport.TransDoc.config;

import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.remote.office.RemoteOfficeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JodConverterConfig {

    @Bean
    public OfficeManager officeManager() {
        return RemoteOfficeManager.builder()
                .urlConnection("http://localhost:2002") // Remote server URL
                .socketTimeout(300000L)                 // Socket timeout in milliseconds
                .connectTimeout(300000L)                // Connection timeout in milliseconds
                .build();
    }
}