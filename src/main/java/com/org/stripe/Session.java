package com.org.stripe;

import lombok.Data;

@Data
public class Session {

	private String id;
	private String url;
	private String status;
	private String payment_status;
}
