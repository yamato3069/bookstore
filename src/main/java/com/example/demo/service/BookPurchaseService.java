package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CartEntity;
import com.example.demo.mapper.BookPurchaseMapper;
import com.example.demo.strategy.BookPurchaseStrategy;

@Service
public class BookPurchaseService {
	
	private final BookPurchaseMapper bookPurchaseMapper;
	private final BookPurchaseStrategy bookPurchaseStrategy;
	
	public BookPurchaseService(BookPurchaseMapper bookPurchaseMapper, BookPurchaseStrategy bookPurchaseStrategy) {
		this.bookPurchaseMapper = bookPurchaseMapper;
		this.bookPurchaseStrategy = bookPurchaseStrategy;
	}

	public List<CartEntity> getCart(Integer userId){
		return bookPurchaseMapper.getCart(userId);
	}
	
	public CartEntity getCartByBookId(Integer userId, Integer bookId) {
		return bookPurchaseMapper.getCartByBookId(userId, bookId);
	}
	
	public void addCart(CartEntity cartEntity){
		bookPurchaseMapper.addCart(cartEntity);
	}
	
	 public void removeItemFromDb(CartEntity cartEntity, Integer userId, Integer bookId) {
		 bookPurchaseStrategy.removeItemFromDb(cartEntity, userId, bookId);
	 }
	 
	 public void removeItemFromSession(Map<Integer, Map<String, Object>> sessionCartList, Integer bookId) {
		 bookPurchaseStrategy.removeItemFromSession(sessionCartList, bookId);
	    }
}
