package com.inventory.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.order.client.InventoryClient;
import com.inventory.order.client.ProductClient;
import com.inventory.order.dto.AddStockRequest;
import com.inventory.order.dto.OrderRequest;
import com.inventory.order.dto.ProductResponse;
import com.inventory.order.dto.ReduceStockRequest;
import com.inventory.order.entity.Order;
import com.inventory.order.entity.OrderStatus;
import com.inventory.order.exception.InventoryException;
import com.inventory.order.exception.OrderNotFoundException;
import com.inventory.order.exception.ProductNotFoundException;
import com.inventory.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderServiceImpl implements OrderService {
	
	private final OrderRepository orderRepo;
	
	private final ProductClient productClient;
	
	private final InventoryClient inventoryClient;
	
	public OrderServiceImpl(OrderRepository orderRepo, ProductClient productClient, InventoryClient inventoryClient)
	{
		this.orderRepo=orderRepo;
		this.productClient=productClient;
		this.inventoryClient=inventoryClient;
	}



	@Override
	@Transactional
	@CircuitBreaker(name = "orderService", fallbackMethod = "orderFallback")
	public String placeOrder(OrderRequest request) {

	    ProductResponse product;

	    try {
	        product = productClient.getProductById(request.getProductId());
	    } catch (FeignException.NotFound e) {
	        throw new ProductNotFoundException("Product not found");
	    } catch (Exception e) {
	        throw new RuntimeException("Product service unavailable");
	    }

	    try {
	        ReduceStockRequest reduceRequest = new ReduceStockRequest();
	        reduceRequest.setProductId(request.getProductId());
	        reduceRequest.setQuantity(request.getQuantity());

	        inventoryClient.reduceStock(reduceRequest);
	    } catch (FeignException e) {
	        throw new InventoryException("Stock unavailable or insufficient");
	    }

	    Order order = new Order();
	    order.setProductId(product.getId());
	    order.setQuantity(request.getQuantity());

	    order.setTotalPrice(
	            product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()))
	    );

	    order.setStatus(OrderStatus.CREATED.toString());

	    orderRepo.save(order);

	    return "Order placed successfully";
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}

	@Override
	public Order getOrderById(Long id) {
	    return orderRepo.findById(id)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found"));
	}
	
	private String orderFallBack(OrderRequest request,Throwable ex)
	{
		return "Order Service is currently unavailable. Please try again later";
	}
	
	@Override
	@Transactional
	public String cancelOrder(Long orderId) {

	    Order order = orderRepo.findById(orderId)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found"));

	    if (!"CREATED".equals(order.getStatus())) {
	        throw new RuntimeException("Order cannot be cancelled");
	    }

	    // Restore stock
	    AddStockRequest addRequest = new AddStockRequest();
	    addRequest.setProductId(order.getProductId());
	    addRequest.setQuantity(order.getQuantity());

	    inventoryClient.addStock(addRequest);

	    // Update status
	    order.setStatus(OrderStatus.CANCELLED.toString());

	    orderRepo.save(order);

	    return "Order cancelled successfully";
	}

}
