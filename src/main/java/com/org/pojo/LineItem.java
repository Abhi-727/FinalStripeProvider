package com.org.pojo;

import lombok.Data;

@Data
public class LineItem {

	private int quantity;
	private String currency;
	private String productName;
	private double unitAmmount;
}
