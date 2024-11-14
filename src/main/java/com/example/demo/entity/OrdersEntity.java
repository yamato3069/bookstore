package com.example.demo.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class OrdersEntity {
	
	public Integer orderId;
	
	public Integer userId;
	
	public Timestamp orderDate;
	
	public BigDecimal totalAmount;

}
