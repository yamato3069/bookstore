package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.BookSearchDto;
import com.example.demo.entity.BookEntity;

@Mapper
public interface BookSearchMapper {
	
	public List<BookEntity> searchBooks(BookSearchDto bookSearchDto);
	
	public BookEntity searchBooksById(Integer bookId);
}
