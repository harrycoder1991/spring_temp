package com.cinfin.bam.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.service.BillingAccountService;

@RestController
@RequestMapping("/api")
public class BillingAccountController {

	private final BillingAccountService billingAccountService;

	public BillingAccountController(BillingAccountService billingAccountService) {
		this.billingAccountService = billingAccountService;
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello from Billing Account API!";
	}
	/*
	 * @PostMapping("/check-and-create") public ResponseEntity<String>
	 * checkAndCreateAccount(@RequestBody AccountInfoDTO accountInfoDTO) { String
	 * response = billingAccountService.checkAndCreateAccount(accountInfoDTO);
	 * return ResponseEntity.ok(response); }
	 */

	@PostMapping(value = "/createBillingAccount", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createBillingAccount(@RequestBody AccountBillDTO accountBill) {
		// Process the incoming AccountBill XML and create the billing account
		// Call the necessary services and handle the business logic here

		// Dummy response for demonstration
		String jsonResponse = "{\"message\": \"Billing account created successfully\"}";

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
}
