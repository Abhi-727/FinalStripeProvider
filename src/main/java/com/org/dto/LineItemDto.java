package com.org.dto;

import lombok.Data;

@Data
public class LineItemDto {

	private int quantity;
	private String currency;
	private String productName;
	private double unitAmmount;
}
