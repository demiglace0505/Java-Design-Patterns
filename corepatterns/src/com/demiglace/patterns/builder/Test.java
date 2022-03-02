package com.demiglace.patterns.builder;

import com.demiglace.patterns.builder.HttpClient.HttpClientBuilder;

public class Test {
	public static void main(String[] args) {
//		HttpClient uglyHttpClient = new HttpClient("GET", "http://test.com", null, null, null, null);
		HttpClientBuilder builder = new HttpClient.HttpClientBuilder();
		HttpClient client = builder.method("POST").url("http://test.com").body("{}").build();
		System.out.println(client);
	}
}
