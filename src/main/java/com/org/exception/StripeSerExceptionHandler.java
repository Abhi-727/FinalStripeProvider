package com.org.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.org.constant.ErrorCodeEnum;
import com.org.pojo.ErrorResponsHandle;

@ControllerAdvice
public class StripeSerExceptionHandler {

	@ExceptionHandler(StripeServiceException.class)
	public ResponseEntity<ErrorResponsHandle> handleException(StripeServiceException ex) {

		ErrorResponsHandle errorResponse = new ErrorResponsHandle();
		errorResponse.setErrorCode(ex.getErrorCode());
		errorResponse.setErrorMessage(ex.getErrorMessage());

		// System.out.println("ErrorResponse : " + errorResponse);

		return new ResponseEntity<>(errorResponse,ex.getHttpstatus());

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponsHandle> handleException(Exception ex) {

		ErrorResponsHandle errorResponse = new ErrorResponsHandle();
		errorResponse.setErrorCode(ErrorCodeEnum.GENERIC_ERROR.getErrorCode());
		errorResponse.setErrorMessage(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());

		System.out.println("ErrorResponse : " + errorResponse);

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
