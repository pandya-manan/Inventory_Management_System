package com.inventory.order.service;

import java.util.List;

import com.inventory.order.dto.OrderRequest;
import com.inventory.order.entity.Order;

public interface OrderService {
	
	String placeOrder(OrderRequest request);
	
	List<Order> getAllOrders();
	
	Order getOrderById(Long id);
	
	String cancelOrder(Long orderId);
}
