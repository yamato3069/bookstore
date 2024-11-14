package com.example.demo.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.BookPurchaseMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginCartService implements CartMethodClass {

	private final BookPurchaseMapper bookPurchaseMapper;

	public LoginCartService(BookPurchaseMapper bookPurchaseMapper) {
		this.bookPurchaseMapper = bookPurchaseMapper;
	}

	public void removeItemFromDb(CartEntity cartEntity, Integer userId, Integer bookId) {
		if (cartEntity != null && cartEntity.getQuantity() > 1) {
			bookPurchaseMapper.updateCartQuantity(userId, bookId, cartEntity.getQuantity() - 1);
		} else {
			bookPurchaseMapper.deleteCartItem(userId, bookId);
		}
	}

	@Override
	public Map<Integer, Map<String, Object>> getCart(HttpSession session, UserEntity user) {

		List<CartEntity> cartEntities = bookPurchaseMapper.getCart(user.getUserId());

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
		bookPurchaseMapper.addCart(cartEntity);
	}

	@Override
	public CartEntity createCartEntity(Map<String, Object> params, UserEntity user, HttpSession session) {
		CartEntity cartEntity = new CartEntity();

		// bookIdがInteger型として返される場合、文字列に変換する
		cartEntity.setBookId(Integer.parseInt(String.valueOf(params.get("bookId"))));
		cartEntity.setQuantity(Integer.parseInt(String.valueOf(params.get("quantity"))));

		// priceもStringとして渡されている場合があるので、確実にStringに変換
		cartEntity.setPrice(new BigDecimal(String.valueOf(params.get("price"))));

		cartEntity.setTitle((String) params.get("title"));
		cartEntity.setAuthorName((String) params.get("authorName"));
		cartEntity.setImagePath((String) params.get("imagePath"));
		cartEntity.setUserId(user.getUserId());

		return cartEntity;
	}

	@Override
	public void addBookToCart(Map<String, Object> params, UserEntity user, HttpSession session) {
		CartEntity cartEntity = createCartEntity(params, user, session);
		addToCart(session, user, cartEntity);
	}

	@Override
	@Transactional
	public void removeItem(HttpSession session, UserEntity user, Integer bookId) {
		CartEntity cartEntity = bookPurchaseMapper.getCartByBookId(user.getUserId(), bookId);
		removeItemFromDb(cartEntity, user.getUserId(), bookId);
	}

	@Override
	public void refreshCart(HttpSession session, UserEntity user) {
		bookPurchaseMapper.refreshCart(user.getUserId());
	}
}
