package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderItemEntity;
import com.example.demo.entity.OrdersEntity;
import com.example.demo.mapper.BookOrderMapper;

@Service
public class BookOrderService {
	
	private final BookOrderMapper bookOrderMapper;
	
	public BookOrderService(BookOrderMapper bookOrderMapper) {
		this.bookOrderMapper = bookOrderMapper;
	}
	
	public Integer insertOrders(OrdersEntity ordersEntity) {
		bookOrderMapper.insertOrders(ordersEntity);
		return ordersEntity.getOrderId();
	}
	
	public void insertOrderItems(OrderItemEntity orderItemEntity) {
		bookOrderMapper.insertOrderItems(orderItemEntity);
	}

}
