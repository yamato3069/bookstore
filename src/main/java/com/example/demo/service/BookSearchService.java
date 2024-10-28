package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookSearchDto;
import com.example.demo.entity.BookEntity;
import com.example.demo.mapper.BookSearchMapper;

@Service
public class BookSearchService {
	
	private final BookSearchMapper bookSearchMapper;
	
	public BookSearchService(BookSearchMapper bookSearchMapper) {
		this.bookSearchMapper = bookSearchMapper;
	}
	
	public List<BookEntity> searchBooks(BookSearchDto bookSearchDto){
		return bookSearchMapper.searchBooks(bookSearchDto);
	}

}
