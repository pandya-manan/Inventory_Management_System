package com.inventory.service;

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
		productClient.getProductById(productId);
	}
	
	
	//Fallback method
	private void productFallBack(Long productId, Throwable ex) {
	    throw new RuntimeException("Product service unavailable. Try again later.");
	}
	
	
	@Override
	public String addStock(AddStockRequest request) {

	    validateProduct(request.getProductId());

	    Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
	            .orElse(null);

	    if (inventory == null) {
	        inventory = new Inventory();
	        inventory.setProductId(request.getProductId());
	        inventory.setQuantity(request.getQuantity());
	    } else {
	        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
	    }

	    inventoryRepository.save(inventory);

	    return "Stock added successfully";
	}

	@Override
	public Integer getStock(Long productId) {

	    validateProduct(productId);

	    Inventory inventory = inventoryRepository.findByProductId(productId)
	            .orElseThrow(() -> new InventoryNotFoundException("Inventory not found"));

	    return inventory.getQuantity();
	}

	@Override
	@Transactional
	public String reduceStock(ReduceStockRequest request) {

	    validateProduct(request.getProductId());

	    Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
	            .orElseThrow(() -> new InventoryNotFoundException("Inventory not found"));

	    if (inventory.getQuantity() < request.getQuantity()) {
	        throw new InsufficientStockException("Not enough stock");
	    }

	    inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
	    inventoryRepository.save(inventory);

	    return "Stock reduced successfully!";
	}

}
