package com.inventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="inventory")
public class Inventory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private Long productId;
	
	@Column(nullable=false)
	private Integer quantity;
	
	private LocalDateTime lastUpdated;
	
	public Inventory() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Inventory(Long id, Long productId, Integer quantity, LocalDateTime lastUpdated) {
		super();
		this.id = id;
		this.productId = productId;
		this.quantity = quantity;
		this.lastUpdated = lastUpdated;
	}
	
	//Auto Update Timestamp
	@PrePersist
	public void onCreate()
	{
		this.lastUpdated=LocalDateTime.now();
	}
	
	@PreUpdate
	public void onUpdate()
	{
		this.lastUpdated=LocalDateTime.now();
	}

}
