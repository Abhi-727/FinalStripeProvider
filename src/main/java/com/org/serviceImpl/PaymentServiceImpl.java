package com.org.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.org.constant.ErrorCodeEnum;
import com.org.dto.CreatePaymentReqDto;
import com.org.dto.CreatePaymentRespDto;
import com.org.dto.LineItemDto;
import com.org.exception.StripeServiceException;
import com.org.http.HttpRequest;
import com.org.http.HttpServiceEngine;
import com.org.service.PaymentService;
import com.org.stripe.Session;
import com.org.stripe.StripeErrorWrapper;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private HttpServiceEngine httpserviceEng;

	@Value("${stripe.createsession.url}")
	private String stripeCreateSessionUrl;

	@Value("${stripe.secretkey}")
	private String stripeSecretkey;

	@Autowired
	private Gson gson;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CreatePaymentRespDto paymentprocess(CreatePaymentReqDto paymentReq) {

		// to do ,invoke stripe
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(stripeSecretkey, "");

		MultiValueMap<String, String> formUrlEncodedRequest = new LinkedMultiValueMap<>();
		formUrlEncodedRequest.add("success_url", paymentReq.getSuccessUrl());
		formUrlEncodedRequest.add("cancel_url", paymentReq.getCancelUrl());
		formUrlEncodedRequest.add("mode", "payment"); // fixed ---?

		for (int i = 0; i < paymentReq.getLineitem().size(); i++) {
			LineItemDto lineItem = paymentReq.getLineitem().get(i);

			int unitAmount = Integer.valueOf((int) (lineItem.getUnitAmmount() * 100));

			formUrlEncodedRequest.add("line_items[" + i + "][price_data][currency]", lineItem.getCurrency());
			formUrlEncodedRequest.add("line_items[" + i + "][price_data][product_data][name]",
					lineItem.getProductName());
			formUrlEncodedRequest.add("line_items[" + i + "][price_data][unit_amount]", String.valueOf(unitAmount));
			formUrlEncodedRequest.add("line_items[" + i + "][quantity]", String.valueOf(lineItem.getQuantity()));

		}
		HttpRequest httpreq = new HttpRequest();
		httpreq.setUrl(stripeCreateSessionUrl);

		httpreq.setMethod(HttpMethod.POST);
		httpreq.setHttpHeaders(httpHeaders);
		httpreq.setRequest(formUrlEncodedRequest);

		ResponseEntity<String> response = httpserviceEng.makeHttpRequest(httpreq);
		System.out.println("response from HTTP SERVICE : " + response);

		if (response.getStatusCode().is2xxSuccessful()) {
			Session session = gson.fromJson(response.getBody(), Session.class);

			if (session.getUrl() != null && !session.getUrl().trim().isEmpty()) {
				System.out.println("Received valid url from stripe : ");// regex
				CreatePaymentRespDto reqResponseDto = mapper.map(session, CreatePaymentRespDto.class);
				System.out.println("converted session to DTO : " + reqResponseDto);

				return reqResponseDto;

			}
			System.out.println("failed to get url");
			// if url is not found handle exception
			throw new StripeServiceException(ErrorCodeEnum.INVALID_SESSION_URL.getErrorCode(),
					ErrorCodeEnum.INVALID_SESSION_URL.getErrorMessage(), HttpStatus.BAD_GATEWAY);

		}

		if (HttpStatus.GATEWAY_TIMEOUT == response.getStatusCode()) {

			// we need to handle
			throw new StripeServiceException(ErrorCodeEnum.UNABLE_TO_CONNECT_WITH_STRIPE.getErrorCode(),
					ErrorCodeEnum.UNABLE_TO_CONNECT_WITH_STRIPE.getErrorMessage(), HttpStatus.GATEWAY_TIMEOUT);
		}
		if (HttpStatus.INTERNAL_SERVER_ERROR == response.getStatusCode()) {

			// handle error
			throw new StripeServiceException(ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
					ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		System.out.println("Got Non2xx code : ");

		StripeErrorWrapper errorResponse = gson.fromJson(response.getBody(), // give error obj
				StripeErrorWrapper.class);

		System.out.println("got errorResponse form stripe | " + errorResponse);

		throw new StripeServiceException(errorResponse.getError().getType(), errorResponse.getError().getMessage(),
				HttpStatus.valueOf(response.getStatusCode().value())); // getting errorcode from response var

	}

}
