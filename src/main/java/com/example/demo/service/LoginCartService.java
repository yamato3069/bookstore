package com.example.demo.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.strategy.BookPurchaseStrategy;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginCartService implements CartMethodClass {
	
	private final BookPurchaseService bookPurchaseService;
	private final BookPurchaseStrategy bookPurchaseStrategy;
	
	public LoginCartService(BookPurchaseService bookPurchaseService, BookPurchaseStrategy bookPurchaseStrategy) {
		this.bookPurchaseService = bookPurchaseService;
		this.bookPurchaseStrategy = bookPurchaseStrategy;
	}

	@Override
	public Map<Integer, Map<String, Object>> getCart(HttpSession session, UserEntity user) {
		List<CartEntity> cartEntities = bookPurchaseService.getCart(user.getUserId());
		Map<Integer, Map<String, Object>> cartList = new HashMap<>();

		for (CartEntity cartEntity : cartEntities) {
			Map<String, Object> item = new HashMap<>();
			item.put("imagePath", cartEntity.getImagePath());
			item.put("price", cartEntity.getPrice());
			item.put("title", cartEntity.getTitle());
			item.put("authorName", cartEntity.getAuthorName());
			item.put("quantity", cartEntity.getQuantity());
			cartList.put(cartEntity.getBookId(), item);
		}

		return cartList;
	}

	@Override
	public void addToCart(HttpSession session, UserEntity user, CartEntity cartEntity) {
		bookPurchaseService.addCart(cartEntity);
	}
	
	@Override
    public CartEntity createCartEntity(Map<String, Object> params, UserEntity user, HttpSession session) {
        CartEntity cartEntity = new CartEntity();
        cartEntity.setBookId(Integer.parseInt((String) params.get("bookId")));
        cartEntity.setQuantity(Integer.parseInt((String) params.get("quantity")));
        cartEntity.setPrice(new BigDecimal((String) params.get("price"))); 
        cartEntity.setTitle((String) params.get("title"));
        cartEntity.setAuthorName((String) params.get("authorName"));
        cartEntity.setImagePath((String) params.get("imagePath"));
        cartEntity.setUserId(user.getUserId());

        return cartEntity;
    }
	
	@Override
	@Transactional
	public void addBookToCart(Map<String, Object> params, UserEntity user, HttpSession session) {
        CartEntity cartEntity = createCartEntity(params, user, session);
        addToCart(session, user, cartEntity);
    }
	
	@Override
	@Transactional
    public void removeItem(HttpSession session, UserEntity user, Integer bookId) {
        CartEntity cartEntity = bookPurchaseService.getCartByBookId(user.getUserId(), bookId);
        bookPurchaseStrategy.removeItemFromDb(cartEntity, user.getUserId(), bookId);
    }


	
}
