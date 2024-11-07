package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.OrderItemEntity;
import com.example.demo.entity.OrdersEntity;

@Mapper
public interface BookOrderMapper {
	
	public Integer insertOrders(OrdersEntity ordersEntity);
	
	public void insertOrderItems(OrderItemEntity orderItemEntity);

}
