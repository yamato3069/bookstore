package com.example.demo.strategy;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CartEntity;
import com.example.demo.mapper.BookPurchaseMapper;

@Service
public class BookPurchaseStrategy {

	private final BookPurchaseMapper bookPurchaseMapper;

	public BookPurchaseStrategy(BookPurchaseMapper bookPurchaseMapper) {
		this.bookPurchaseMapper = bookPurchaseMapper;
	}

	public void removeItemFromDb(CartEntity cartEntity, Integer userId, Integer bookId) {
		if (cartEntity != null && cartEntity.getQuantity() > 1) {
			bookPurchaseMapper.updateCartQuantity(userId, bookId, cartEntity.getQuantity() - 1);
		} else {
			bookPurchaseMapper.deleteCartItem(userId, bookId);
		}
	}

	public void removeItemFromSession(Map<Integer, Map<String, Object>> sessionCartList, Integer bookId) {
		Map<String, Object> cartItem = sessionCartList.get(bookId);
		if (cartItem != null) {
			Integer quantity = (Integer) cartItem.get("quantity");
			if (quantity != null) {
				if (quantity > 1) {
					cartItem.put("quantity", quantity - 1);
				} else {
					sessionCartList.remove(bookId);
				}
			}
		}
	}

}
