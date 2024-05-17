package com.e_commerce.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

	@Value("${spring.data.elasticsearch.uris}")
	private String elasticsearchUris;

	@Value("${spring.data.elasticsearch.username}")
	private String username;

	@Value("${spring.data.elasticsearch.password}")
	private String password;

	@Bean
	public RestHighLevelClient restHighLevelClient() {
		RestClientBuilder builder = RestClient.builder(HttpHost.create(elasticsearchUris))
			.setHttpClientConfigCallback(httpClientBuilder ->
				httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider(username, password)));

		return new RestHighLevelClient(builder);
	}

	private CredentialsProvider credentialsProvider(String username, String password) {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
			new UsernamePasswordCredentials(username, password));
		return credentialsProvider;
	}
}

