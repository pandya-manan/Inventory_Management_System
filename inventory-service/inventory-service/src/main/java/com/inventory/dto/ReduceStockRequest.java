package com.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReduceStockRequest {
	
	@NotNull(message="Proudct Id is required")
	private Long productId;
	
	@NotNull(message="Quantity is mandatory and required")
	@Min(value=1,message="Quantity must be atleast 1")
	private Integer quantity;
	
	public ReduceStockRequest() {}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	

}
