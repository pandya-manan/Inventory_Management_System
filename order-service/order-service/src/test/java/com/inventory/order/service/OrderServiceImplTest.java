package com.inventory.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.inventory.order.client.InventoryClient;
import com.inventory.order.client.ProductClient;
import com.inventory.order.dto.OrderRequest;
import com.inventory.order.dto.ProductResponse;
import com.inventory.order.entity.Order;
import com.inventory.order.entity.OrderStatus;
import com.inventory.order.repository.OrderRepository;

public class OrderServiceImplTest {
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private ProductClient productClient;
	
	@Mock
	private InventoryClient inventoryClient;
	
	@InjectMocks
	private OrderServiceImpl orderService;
	
	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void placeOrder_success()
	{
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setProductId(1L);
		orderRequest.setQuantity(2);
		
		ProductResponse response = new ProductResponse();
		response.setId(1L);
		response.setPrice(BigDecimal.valueOf(500));
		
		when(productClient.getProductById(1L)).thenReturn(response);
		when(inventoryClient.reduceStock(any())).thenReturn("success");
		
		String result = orderService.placeOrder(orderRequest);
		assertEquals("Order placed successfully",result);
		verify(orderRepository,times(1)).save(any(Order.class));
	}
	
	@Test
	void placeOrder_productNotFound()
	{
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setProductId(1L);
		orderRequest.setQuantity(2);
		when(productClient.getProductById(1L)).thenThrow(new RuntimeException());
		assertThrows(RuntimeException.class,()->{
			orderService.placeOrder(orderRequest);
		});
	}
	
	@Test
	void placeOrder_inventoryFailure()
	{
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setProductId(1L);
		orderRequest.setQuantity(2);
		
		ProductResponse response = new ProductResponse();
		response.setId(1L);
		response.setPrice(BigDecimal.valueOf(500));
		
		when(productClient.getProductById(1L)).thenReturn(response);
		when(inventoryClient.reduceStock(any())).thenThrow(new RuntimeException());
		assertThrows(RuntimeException.class,()->{
			orderService.placeOrder(orderRequest);
		});
	}
	
	@Test
	void cancelOrder_success()
	{
		Order order = new Order();
		order.setId(1L);
		order.setProductId(1L);
		order.setQuantity(2);
		order.setStatus(OrderStatus.CREATED.toString());
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		String result = orderService.cancelOrder(1L);
		assertEquals("Order cancelled successfully",result);
		verify(inventoryClient,times(1)).addStock(any());		
	}
	
	@Test
	void cancelOrder_alreadyCancelled()
	{
		Order order = new Order();
		order.setId(1L);
		order.setProductId(1L);
		order.setQuantity(2);
		order.setStatus(OrderStatus.CANCELLED.toString());
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		assertThrows(RuntimeException.class,()->{
			orderService.cancelOrder(1L);
		});
	}
	
	@Test
	void cancelOrder_notFound()
	{
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class,()->{
			orderService.cancelOrder(1L);
		});
	}

}
