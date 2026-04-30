package com.inventory.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
	

    private final OrderRepository orderRepo;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    public OrderServiceImpl(OrderRepository orderRepo,
                             ProductClient productClient,
                             InventoryClient inventoryClient) {
        this.orderRepo = orderRepo;
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
    }

    // ==============================
    // ORDER PLACEMENT
    // ==============================

    @Override
    @Transactional
    public String placeOrder(OrderRequest request) {
    	
    	log.info("Placing order: productId={}, quantity={}", request.getProductId(), request.getQuantity());

        ProductResponse product = getProduct(request.getProductId());

        log.debug("Product fetched successfully: productId={}", product.getId());
        ReduceStockRequest reduceRequest = new ReduceStockRequest();
        reduceRequest.setProductId(request.getProductId());
        reduceRequest.setQuantity(request.getQuantity());

        reduceStock(reduceRequest);
        log.debug("Stock reduced successfully for productId={}", request.getProductId());

        Order order = new Order();
        order.setProductId(product.getId());
        order.setQuantity(request.getQuantity());

        order.setTotalPrice(
                product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()))
        );

        order.setStatus(OrderStatus.CREATED.toString());

        orderRepo.save(order);
        
        log.info("Order placed successfully: orderId={}, productId={}", order.getId(), product.getId());

        return "Order placed successfully";
    }

    // ==============================
    // PRODUCT SERVICE CALL
    // ==============================

    @CircuitBreaker(name = "productService", fallbackMethod = "productFallback")
    private ProductResponse getProduct(Long productId) {

        try {
        	log.debug("Calling Product Service for productId={}", productId);
            return productClient.getProductById(productId);
        } catch (FeignException.NotFound e) {
        	log.error("Product not found: productId={}", productId, e);
            throw new ProductNotFoundException("Product not found");
        } catch (Exception e) {
        	log.error("Product service unavailable: productId={}", productId, e);
            throw new RuntimeException("Product service unavailable");
        }
    }

    @SuppressWarnings("unused")
	private ProductResponse productFallback(Long productId, Throwable ex) {
    	log.error("Product service fallback triggered for productId={}", productId, ex);
        throw new RuntimeException("Product service is currently unavailable");
    }

    // ==============================
    // INVENTORY SERVICE CALL
    // ==============================

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    private void reduceStock(ReduceStockRequest request) {

        try {
        	log.debug("Calling Inventory Service to reduce stock: productId={}, quantity={}",
        	          request.getProductId(), request.getQuantity());
            inventoryClient.reduceStock(request);
        } catch (FeignException e) {
        	log.error("Stock unavailable or insufficient: productId={}, quantity={}",
                    request.getProductId(), request.getQuantity(), e);
            throw new InventoryException("Stock unavailable or insufficient");
        }
    }

    @SuppressWarnings("unused")
	private void inventoryFallback(ReduceStockRequest request, Throwable ex) {
    	log.error("Inventory service fallback triggered for productId={}", request.getProductId(), ex);
        throw new RuntimeException("Inventory service is currently unavailable");
    }

    // ==============================
    // FETCH ORDERS
    // ==============================

    @Override
    public List<Order> getAllOrders() {
    	log.info("Fetching all orders");
        return orderRepo.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
    	log.info("Fetching order: orderId={}", id);
        return orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    // ==============================
    // CANCEL ORDER (COMPENSATION)
    // ==============================

    @Override
    @Transactional
    public String cancelOrder(Long orderId) {

    	log.info("Cancelling order: orderId={}", orderId);
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (!OrderStatus.CREATED.toString().equals(order.getStatus())) {
        	log.warn("Order cannot be cancelled: orderId={}, status={}", orderId, order.getStatus());
            throw new RuntimeException("Order cannot be cancelled");
        }

        AddStockRequest addRequest = new AddStockRequest();
        addRequest.setProductId(order.getProductId());
        addRequest.setQuantity(order.getQuantity());

        try {
        	log.debug("Calling Inventory Service to restore stock: productId={}, quantity={}",
        	          order.getProductId(), order.getQuantity());
            inventoryClient.addStock(addRequest);
        } catch (Exception e) {
        	log.error("Failed to restore stock: productId={}, quantity={}",
                    order.getProductId(), order.getQuantity(), e);
            throw new RuntimeException("Failed to restore stock");
        }

        order.setStatus(OrderStatus.CANCELLED.toString());
        orderRepo.save(order);

        log.info("Order cancelled successfully: orderId={}", orderId);
        return "Order cancelled successfully";
    }
}