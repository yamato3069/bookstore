package com.example.demo.entity;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

@Data
public class BookEntity {
	
	public Integer bookId;
	
	public String title;
	
	public Integer authorId;
	
	private String authorName;
	
	public Integer categoryId;
	
	public BigDecimal price;
	
	public String description;
	
	public Date publishedDate;
	
	public Integer stock;
	
	public String imagePath;

}
