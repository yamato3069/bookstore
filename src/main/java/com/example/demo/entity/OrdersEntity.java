package com.example.demo.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OrdersEntity {
	
	public Integer orderId;
	
	public Integer userId;
	
	public Date orderDate;
	
	public BigDecimal totalAmount;

}
