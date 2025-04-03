package com.rsupport.TransDoc.config;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.remote.RemoteConverter;
import org.jodconverter.remote.office.RemoteOfficeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JodConverterConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Primary
    public RemoteOfficeManager officeManager() {
        return RemoteOfficeManager.builder()
                .urlConnection("http://localhost:2002") // URL of the remote LibreOffice server
                .socketTimeout(300000L)                 // Socket timeout in milliseconds
                .connectTimeout(300000L)                // Connection timeout in milliseconds
                .build();
    }

    @Bean
    public DocumentConverter documentConverter(RemoteOfficeManager officeManager) {
        return RemoteConverter.make(officeManager);
    }
}
