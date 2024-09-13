package com.ictedu.springrestapi.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;

import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

//RestTemplate 커넥션 풀을 사용하기 위한 빈 등록
//사전 작업:build.gradle에 아래 등록(RestTemplate사용시 커넥션 풀을 사용하기 위함)
//implementation 'org.apache.httpcomponents:httpclient'
@Configuration
public class RestTemplateConfig {
	
	//https://velog.io/@chiyongs/Spring-Boot-3.x-%EC%97%85%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%9C-%EC%8B%9C-Apache-HttpClient-%EC%9D%98%EC%A1%B4%EC%84%B1-%EB%AC%B8%EC%A0%9C
	static final int READ_TIMEOUT = 1500;
	static final int CONN_TIMEOUT = 3000;

	@Bean
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(createHttpClient());
	    return new RestTemplate(factory);
	}

	private HttpClient createHttpClient() {
		return HttpClientBuilder.create()
	                      .setConnectionManager(createHttpClientConnectionManager())
	                      .build();
	}

	private HttpClientConnectionManager createHttpClientConnectionManager() {
		return PoolingHttpClientConnectionManagerBuilder.create()
	                                                    .setDefaultConnectionConfig(ConnectionConfig.custom()
	                                                                                                .setSocketTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
	                                                                                                .setConnectTimeout(CONN_TIMEOUT, TimeUnit.MILLISECONDS)
	                                                                                                .build())
	                                                    .build();
	}
}
