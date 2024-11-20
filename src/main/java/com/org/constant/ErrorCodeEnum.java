package com.org.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

	GENERIC_ERROR("30000","Unable to process,try later"),
	INVALID_SESSION_URL("30001" ," Invalid session url received from stripe" ),
	UNABLE_TO_CONNECT_WITH_STRIPE("30002", "Unable to connect with Stripe. Please try later"),
	
	TXN_STATUS_HANDLER_NOT_CONFIGURED("20002", "StatusHandler not configured, Try again later."),
	DUPLICATE_TXN_REFERENCE("20003" , " Duplicate Txn is Entered");
	
	private final String errorCode;
	private final String errorMessage;
	
	ErrorCodeEnum(String errorCode , String errorMessage ){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
}
