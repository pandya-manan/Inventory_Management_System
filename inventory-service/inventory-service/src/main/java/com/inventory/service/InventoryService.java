package com.inventory.service;

import com.inventory.dto.AddStockRequest;
import com.inventory.dto.ReduceStockRequest;

public interface InventoryService {
	
	String addStock(AddStockRequest request);
	
	Integer getStock(Long productId);
	
	String reduceStock(ReduceStockRequest request);

}
