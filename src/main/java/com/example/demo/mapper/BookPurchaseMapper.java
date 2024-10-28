package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.CartEntity;

@Mapper
public interface BookPurchaseMapper {
	
	public List<CartEntity> getCart(Integer userId);
	
	public CartEntity getCartByBookId(Integer userId, Integer bookId);
	
	public void addCart(CartEntity cartEntity);
	
	public void removeItemFromDb(Integer userId, Integer bookId);
	
	public void updateCartQuantity(Integer userId, Integer bookId, Integer quantity);
	
	public void deleteCartItem(Integer userId, Integer bookId);

}
