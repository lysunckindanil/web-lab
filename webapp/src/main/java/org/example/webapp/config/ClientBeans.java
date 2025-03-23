package org.example.webapp.config;

import org.example.webapp.client.CommentRestClientImpl;
import org.example.webapp.client.IssueRestClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {
    @Bean
    public IssueRestClientImpl issueRestClient(@Value("${issues.client.base-url:http://localhost:1337}") String baseUrl) {
        return new IssueRestClientImpl(RestClient.builder()
                .baseUrl(baseUrl).build());
    }

    @Bean
    public CommentRestClientImpl commentRestClient(@Value("${comments.client.base-url:http://localhost:1337}") String baseUrl) {
        return new CommentRestClientImpl(RestClient.builder()
                .baseUrl(baseUrl).build());
    }
}
