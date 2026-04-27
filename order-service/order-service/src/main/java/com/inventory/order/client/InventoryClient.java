package com.inventory.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.inventory.order.dto.AddStockRequest;
import com.inventory.order.dto.ReduceStockRequest;

@FeignClient(name="inventory-service",url="http://localhost:8083")
public interface InventoryClient {
	
	@PostMapping("/inventory/reduce")
	String reduceStock(@RequestBody ReduceStockRequest request);
	
	@PostMapping("/inventory/add")
	String addStock(@RequestBody AddStockRequest request);
	

}
