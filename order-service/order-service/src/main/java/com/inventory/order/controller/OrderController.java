package com.inventory.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.order.dto.OrderRequest;
import com.inventory.order.entity.Order;
import com.inventory.order.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderService orderService;
	
	public OrderController(OrderService orderService)
	{
		this.orderService=orderService;
	}
	
	@PostMapping
	public String placeOrder(@Valid @RequestBody OrderRequest orderRequest)
	{
		return orderService.placeOrder(orderRequest);
	}
	
	@GetMapping
	public List<Order> getAllOrders()
	{
		return orderService.getAllOrders();
	}

	@GetMapping("/{id}")
	public Order getOrderById(@PathVariable Long id)
	{
		return orderService.getOrderById(id);
	}
	
	@PutMapping("/{id}")
	public String cancelOrder(@PathVariable Long id)
	{
		return orderService.cancelOrder(id);
	}
}
