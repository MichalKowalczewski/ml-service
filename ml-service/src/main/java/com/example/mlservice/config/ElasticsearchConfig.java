package com.example.mlservice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ElasticsearchConfig {

    @Value("${elastic.host}")
    private String elasticHost;
    @Value("${elastic.port}")
    private int elasticPort;
    @Value("${elastic.user}")
    private String elasticUser;
    @Value("${elastic.pass}")
    private String elasticPass;
    @Value("${elastic.scheme}")
    private String elasticScheme;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return new ElasticsearchClient(
                configureRestClientTransport(),
                getRestClientOptions()
        );
    }


    private RestClient configureRestClient() {
        return RestClient.builder(new HttpHost(elasticHost, elasticPort, elasticScheme))
                .setRequestConfigCallback(builder -> builder
                        .setConnectTimeout(5000)
                        .setSocketTimeout(60000)
                        .setConnectionRequestTimeout(30000))
                //.setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                //        .setDefaultCredentialsProvider(configureCredentialsProvider()))
                .build();
    }

    private RestClientTransport configureRestClientTransport() {
        return new RestClientTransport(
                configureRestClient(), new JacksonJsonpMapper()
        );
    }

    private RestClientOptions getRestClientOptions() {
        RequestOptions.Builder reuestOptionsBuilder = RequestOptions.DEFAULT.toBuilder();

        reuestOptionsBuilder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(905539255));
        return new RestClientOptions(reuestOptionsBuilder.build());
    }

    private CredentialsProvider configureCredentialsProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticUser, elasticPass));
        return credentialsProvider;
    }


}
