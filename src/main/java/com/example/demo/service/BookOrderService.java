package com.example.demo.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.OrderItemEntity;
import com.example.demo.entity.OrdersEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.BookOrderMapper;

@Service
public class BookOrderService {

	private final BookOrderMapper bookOrderMapper;

	public BookOrderService(BookOrderMapper bookOrderMapper) {
		this.bookOrderMapper = bookOrderMapper;
	}

	//オーダーエンティティの作成
	public OrdersEntity createOrder(Map<Integer, Map<String, Object>> cartList, UserEntity user) {

		LocalDateTime currentDateTime = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(currentDateTime);

		BigDecimal totalAmount = BigDecimal.ZERO;

		for (Map.Entry<Integer, Map<String, Object>> entry : cartList.entrySet()) {
			Map<String, Object> item = entry.getValue();

			// アイテムの価格と数量を取得し、合計金額を計算
			BigDecimal price = (BigDecimal) item.get("price");
			Integer quantity = (Integer) item.get("quantity");
			totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
		}

		OrdersEntity order = new OrdersEntity();
		order.setUserId(user.getUserId());
		order.setOrderDate(timestamp);
		order.setTotalAmount(totalAmount);

		return order;
	}

	//オーダーアイテムエンティティの作成
	public OrderItemEntity createOrderItem(Map<Integer, Map<String, Object>> cartList, Integer orderId) {
		OrderItemEntity item = new OrderItemEntity();

		for (Map.Entry<Integer, Map<String, Object>> entry : cartList.entrySet()) {
			Map<String, Object> cartItem = entry.getValue();
			Integer bookId = Integer.parseInt(entry.getKey().toString());
			item.setOrderId(orderId);
			item.setBookId(bookId);
			item.setQuantity((Integer) cartItem.get("quantity"));
			item.setPrice((BigDecimal) cartItem.get("price"));
		}

		return item;
	}

	//オーダーテーブルへの登録処理
	public Integer insertOrders(OrdersEntity ordersEntity) {
		bookOrderMapper.insertOrders(ordersEntity);
		return ordersEntity.getOrderId();
	}

	//オーダーアイテムテーブルへの登録処理
	public void insertOrderItems(OrderItemEntity orderItemEntity) {
		bookOrderMapper.insertOrderItems(orderItemEntity);
	}
	
	
	@Transactional
	public void orderMethod(Map<Integer, Map<String, Object>> cartList, UserEntity user) {
		OrdersEntity orderEntity = createOrder(cartList, user);
		Integer orderId = insertOrders(orderEntity);
		OrderItemEntity orderItemEntity = createOrderItem(cartList, orderId);
		insertOrderItems(orderItemEntity);	
	}

}
