package com.org.service;

import com.org.dto.CreatePaymentReqDto;
import com.org.dto.CreatePaymentRespDto;

public interface PaymentService {
	
	public CreatePaymentRespDto paymentprocess(CreatePaymentReqDto paymentReq);
}
