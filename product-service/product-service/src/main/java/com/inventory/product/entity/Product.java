package com.inventory.product.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="products")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Schema(name="id",example="1")
	private Long id;
	
	@Schema(name="name",example="Dell Laptop")
	private String name;
	
	@Schema(name="description",example="Dell Laptop 512GB SSD, 12GB RAM")
	private String description;
	
	@Schema(name="price",example="90500.50")
	private BigDecimal price;
	
	@Schema(name="sku",example="DELL-LAPTOP-512GB")
	private String sku;
	
	@Schema(name="category",example="Electronics")
	private String category;
	
	@Schema(name="createdAt",example="2026-04-21T13:09:44")
	private LocalDateTime createdAt;
	
	@Schema(name = "updatedAt", example = "2026-04-21T13:09:44")
	private LocalDateTime updatedAt;
	
	public Product() {}

	
	//Getters and Setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@PrePersist
	public void onCreate() {
	    this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
	    this.updatedAt = LocalDateTime.now();
	}


	public Product(Long id, String name, String description, BigDecimal price, String sku, String category,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.sku = sku;
		this.category = category;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	

}
