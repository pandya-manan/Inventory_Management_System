package com.inventory.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.inventory.order.dto.ProductResponse;

@FeignClient(name="product-service",url="http://localhost:8082")
public interface ProductClient {
	
	@GetMapping("/products/{id}")
    ProductResponse getProductById(@PathVariable Long id);
	

}
