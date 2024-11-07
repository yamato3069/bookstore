package com.example.demo.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemEntity {
	
	public Integer orderItemId;
	
	public Integer orderId;
	
	public Integer bookId;
	
	public Integer quantity;
	
	public BigDecimal price;

}
