package com.example.demo.dto;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class BookSearchDto {
	
	@Nullable
	public String title;
	
	@Nullable
	public String authorName;
	
	@Nullable
	public String categoryName;
	
}
