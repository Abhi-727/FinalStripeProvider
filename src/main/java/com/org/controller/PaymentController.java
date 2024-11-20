package com.org.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.dto.CreatePaymentReqDto;
import com.org.dto.CreatePaymentRespDto;
import com.org.pojo.CreatePaymentReq;
import com.org.pojo.CreatePaymentResp;
import com.org.service.PaymentService;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

	@Autowired
	private ModelMapper modelmapper;
	
	@Autowired
	private PaymentService service;

	@PostMapping("/payment")
	public ResponseEntity<CreatePaymentResp> createpayment(@RequestBody CreatePaymentReq paymentReq) {
		System.out.println("PaymentController.createpayment()" + "| paymentReq :" + paymentReq);

		CreatePaymentReqDto reqDto = modelmapper.map(paymentReq, CreatePaymentReqDto.class);
		System.out.println("converted pojo into dto : " + reqDto);
		
		CreatePaymentRespDto serviceResponse = service.paymentprocess(reqDto);
		
		CreatePaymentResp finalresponse = modelmapper.map(serviceResponse, CreatePaymentResp.class);
		System.out.println("converted dto to response :  |" + finalresponse);

		

		return new ResponseEntity<CreatePaymentResp>(finalresponse, HttpStatus.CREATED);
	}
}
