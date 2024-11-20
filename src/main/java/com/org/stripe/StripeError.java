package com.org.stripe;

import lombok.Data;

@Data
public class StripeError {

	 private String type;
	 private String code;
	 private String decline_code;
	 private String param;
	 private String message;
}
