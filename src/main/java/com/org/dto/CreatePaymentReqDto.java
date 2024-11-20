package com.org.dto;

import java.util.List;

import com.org.pojo.LineItem;

import lombok.Data;

@Data
public class CreatePaymentReqDto {

	private String txnRef;
	
	private List<LineItemDto> lineitem;
	private String successUrl;
	private String cancelUrl;
}
