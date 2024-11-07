package com.example.demo.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.strategy.BookPurchaseStrategy;

import jakarta.servlet.http.HttpSession;

@Service
public class GuestCartService implements CartMethodClass {
	
	private final BookPurchaseStrategy bookPurchaseStrategy;
	
	public GuestCartService(BookPurchaseStrategy bookPurchaseStrategy) {
		this.bookPurchaseStrategy = bookPurchaseStrategy;
	}

    @Override
    public Map<Integer, Map<String, Object>> getCart(HttpSession session, UserEntity user) {
        @SuppressWarnings("unchecked")
        Map<Integer, Map<String, Object>> cartList = (Map<Integer, Map<String, Object>>) session.getAttribute("cartList");
        return cartList != null ? cartList : new HashMap<>();
    }

    @Override
    public void addToCart(HttpSession session, UserEntity user, CartEntity cartEntity) {
        @SuppressWarnings("unchecked")
        Map<Integer, Map<String, Object>> cartList = (Map<Integer, Map<String, Object>>) session.getAttribute("cartList");
        if (cartList == null) {
            cartList = new HashMap<>();
        }
        
        Map<String, Object> item = new HashMap<>();
        //↓ゲストのuserテーブルでの扱い方を考える必要がある。
        item.put("userId", 99);
        item.put("imagePath", cartEntity.getImagePath());
        item.put("price", cartEntity.getPrice());
        item.put("title", cartEntity.getTitle());
        item.put("authorName", cartEntity.getAuthorName());
        item.put("quantity", cartEntity.getQuantity());
        
        cartList.put(cartEntity.getBookId(), item);
        session.setAttribute("cartList", cartList);
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
        cartEntity.setUserId(session.getId().hashCode());

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
        @SuppressWarnings("unchecked")
        Map<Integer, Map<String, Object>> cartList = (Map<Integer, Map<String, Object>>) session.getAttribute("cartList");
        bookPurchaseStrategy.removeItemFromSession(cartList, bookId);
        session.setAttribute("cartList", cartList);
    }
}

