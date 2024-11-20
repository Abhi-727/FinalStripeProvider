package com.org.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class StripeServiceException  extends RuntimeException{

	
	private static final long serialVersionUID = 8782134243956606048L;
	
	private final String errorCode;
	private final  String errorMessage;
	private final HttpStatus httpstatus;
}
