package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.BookEntity;
import com.example.demo.service.BookSearchService;

@Controller
@RequestMapping("bookdetail")
public class BookDetailController {
	
	private final BookSearchService bookSearchService;
	
	public BookDetailController(BookSearchService bookSearchService) {
		this.bookSearchService = bookSearchService;
	}
	
	@GetMapping("/bookDetail/{id}")
	public String getBookDetails(@PathVariable("id") String bookId, Model model) {
		Integer id = Integer.parseInt(bookId);
	    BookEntity book = bookSearchService.searchBooksById(id);
	    model.addAttribute("book", book); 
	    return "bookdetail/bookDetail"; 
	}
}
