package com.inventory.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddStockRequest {
	
	@NotNull(message="Product ID is required")
	private Long productId;
	
	@NotNull(message="Quantity is required and mandatory")
	@Min(value=1,message="Quantity minimum value is 1")
	private Integer quantity;
	
	public AddStockRequest() {}

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

	@Override
	public String toString() {
		return "AddStockRequest [productId=" + productId + ", quantity=" + quantity + "]";
	}
	
	

}
