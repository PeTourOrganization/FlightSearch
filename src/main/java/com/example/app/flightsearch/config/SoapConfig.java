package com.example.app.flightsearch.config;

import com.example.app.flightsearch.service.SearchRequest;
import com.example.app.flightsearch.service.SearchResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class SoapConfig {
    @Bean
    public Jaxb2Marshaller marshaller() {
        var marshaller = new Jaxb2Marshaller();
//        marshaller.setContextPath("com.example.app.flightsearch.service");
        marshaller.setClassesToBeBound(SearchRequest.class, SearchResult.class);
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        var template = new WebServiceTemplate(marshaller);
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        return template;
    }

}
