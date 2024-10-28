package com.example.demo.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartEntity {
	
	public Integer cartId; 
	
    public Integer userId; 

	public Integer bookId;

	public String title;

	public String authorName;
	
	public BigDecimal price;

	public Integer quantity;
	
	public String imagePath;
}
