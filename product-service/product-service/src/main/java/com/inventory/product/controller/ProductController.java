package com.inventory.product.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.product.dto.ProductRequest;
import com.inventory.product.entity.Product;
import com.inventory.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/products")
@Tag(name = "Products API")
public class ProductController {
	
	private final ProductService productService;
	
	public ProductController (ProductService productService)
	{
		this.productService=productService;
	}

	@Operation(summary="Add a new product",description="Adds a new product")
	@ApiResponses(value= {
		@ApiResponse(responseCode="200",description="Successfully added a new product"),
		@ApiResponse(responseCode="400",description="Bad Request")
	})
	@PostMapping
	public String addProduct(@Valid @RequestBody ProductRequest productRequest)
	{
		return productService.addProduct(productRequest);
	}
	
	@Operation(summary="Get all products",description="Get all the products currently available")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="Successfully retrieved all products"),
			@ApiResponse(responseCode="500",description="Internal Server Error")
	})
	@GetMapping
	public List<Product> getProducts()
	{
		return productService.getProducts();
	}
	
	@Operation(summary="Get Product By Product Id",description="Get Product By Product Id")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="Product retrieved by product id"),
			@ApiResponse(responseCode="404",description="Product not found for the given product id")
	})
	@GetMapping("/{id}")
	public Product getProductById(@PathVariable Long id)
	{
		return productService.getProductById(id);
	}
	
	@Operation(summary="Update Product",description="Update an existing product")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="Product Updated successfully"),
			@ApiResponse(responseCode="404",description="Product not found for the given product id, hence cannot be updated")
	})
	@PutMapping("/{id}")
	public String updateProduct(@PathVariable Long id,@Valid @RequestBody ProductRequest productRequest)
	{
		return productService.updateProduct(id, productRequest);
	}
	
	@Operation(summary="Delete Product",description="Delete product based on product id")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="Product Deleted Successfully"),
			@ApiResponse(responseCode="404",description="Product not found for the given product id, hence cannot be deleted")
	})
	@DeleteMapping("/{id}")
	public String deleteProduct(@PathVariable Long id)
	{
		return productService.deleteProduct(id);
	}
}
