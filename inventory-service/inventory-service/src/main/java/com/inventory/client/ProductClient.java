package com.inventory.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.inventory.config.FeignConfig;

@FeignClient(name="product-service",url="http://localhost:8082",configuration=FeignConfig.class)
public interface ProductClient {
	
	@GetMapping("/products/{id}")
	Object getProductById(@PathVariable Long id);

}
