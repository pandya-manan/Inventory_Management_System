package com.inventory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.inventory.client.ProductClient;
import com.inventory.dto.AddStockRequest;
import com.inventory.dto.ReduceStockRequest;
import com.inventory.entity.Inventory;
import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.InventoryNotFoundException;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.repository.InventoryRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import jakarta.transaction.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);	
	private final InventoryRepository inventoryRepository;
	
	private final ProductClient productClient;
	
	public InventoryServiceImpl(InventoryRepository inventoryRepository,ProductClient productClient)
	{
		this.inventoryRepository=inventoryRepository;
		this.productClient=productClient;
	}

	//Validate product method
	@CircuitBreaker(name="productService",fallbackMethod="productFallBack")
	private void validateProduct(Long productId)
	{
		log.debug("Calling Product Service for productId={}",productId);
		productClient.getProductById(productId);
	}
	
	
	//Fallback method
	private void productFallBack(Long productId, Throwable ex) {
		log.error("Product service fallback triggered for productId={}",productId);
	    throw new RuntimeException("Product service unavailable. Try again later.");
	}
	
	
	@Override
	public String addStock(AddStockRequest request) {

		log.info("Adding Stock: productId={}, quantity={}",request.getProductId(),request.getQuantity());
	    validateProduct(request.getProductId());

	    Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
	            .orElse(null);

	    if (inventory == null) {
	    	log.debug("Creating new inventory");
	        inventory = new Inventory();
	        inventory.setProductId(request.getProductId());
	        inventory.setQuantity(request.getQuantity());
	        log.info("New inventory created: productId={}, quantity={}",inventory.getProductId(),inventory.getQuantity());
	    } else {
	    	log.info("Inventory updated: productId={},quantity={}",request.getProductId(),request.getQuantity());
	        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
	    }

	    inventoryRepository.save(inventory);
	    log.info("Inventory saved: productId={},quantity={}",inventory.getProductId(),inventory.getQuantity());

	    return "Stock added successfully";
	}

	@Override
	public Integer getStock(Long productId) {

		log.info("Fetching stock: productId={}",productId);
	    validateProduct(productId);

	    Inventory inventory = inventoryRepository.findByProductId(productId)
	            .orElseThrow(() -> new InventoryNotFoundException("Inventory not found"));

	    log.info("Stock found: productId={},quantity={}",inventory.getProductId(),inventory.getQuantity());
	    return inventory.getQuantity();
	}

	@Override
	@Transactional
	public String reduceStock(ReduceStockRequest request) {

		log.info("Reducing stock: productId={},quantity={}",request.getProductId(),request.getQuantity());
	    validateProduct(request.getProductId());

	    Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
	            .orElseThrow(() -> new InventoryNotFoundException("Inventory not found"));

	    if (inventory.getQuantity() < request.getQuantity()) {
	    	log.error("Insufficient stock: productId={},availableQuantity={},requestQuantity={}",request.getProductId(),inventory.getQuantity(),request.getQuantity());
	        throw new InsufficientStockException("Not enough stock");
	    }

	    inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
	    inventoryRepository.save(inventory);
	    log.info("Stock reduced successfully: productId={},quantity={}",request.getProductId(),inventory.getQuantity());
	    return "Stock reduced successfully!";
	}

}
