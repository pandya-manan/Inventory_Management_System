package com.inventory.order.dto;

public class ReduceStockRequest {
	
	private Long productId;
	private Integer quantity;
	
	public ReduceStockRequest()
	{
		
	}

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
