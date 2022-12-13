package com.tgerstel.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReceiptControllerHttpRquestTest {
	
	@Autowired
	private ReceiptController receiptController;
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contexLoads() {
		Assertions.assertThat(receiptController).isNotNull();
	}
	
//	@Test
//	public void greetingShouldReturnDefaultMessage() throws Exception {
//		Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
//				String.class)).contains("Hello, World");
//	}
	

	@Test
	void testAllReceipts() {
		fail("Not yet implemented");
	}

	@Test
	void testGetReceiptById() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testSearchReceipts() {
		fail("Not yet implemented");
	}

}
