package com.org.dto;

import lombok.Data;

@Data
public class SessionDto {

	private String id;
	private String url;
	private String status;
	private String payment_status;
}
