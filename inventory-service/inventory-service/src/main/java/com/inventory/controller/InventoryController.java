package com.inventory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.dto.AddStockRequest;
import com.inventory.dto.ReduceStockRequest;
import com.inventory.service.InventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/inventory")
@Tag(name="Inventory API")
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	public InventoryController(InventoryService inventoryService)
	{
		this.inventoryService=inventoryService;
	}
	
	@Operation(summary="Add Stock for a product",description="Adds stock quantity for a product")
	@ApiResponses(value= {
		@ApiResponse(responseCode="200",description="Added stock for a product"),
		@ApiResponse(responseCode="500",description="Internal Server Error"),
		@ApiResponse(responseCode="400",description="Method Not Valid Exception")
	})
	@PostMapping("/add")
	public String addStock(@Valid @RequestBody AddStockRequest request)
	{
		return inventoryService.addStock(request);
	}
	
	@Operation(summary="Get Stock for a product",description="Get Stock quantity currently present for a given product")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="Fetched Stock for a given product"),
			@ApiResponse(responseCode="404",description="Inventory Not Found for the given product"),
			@ApiResponse(responseCode="500",description="Internal Server Error")
	})
	@GetMapping("/{productId}")
	public Integer getStock(@PathVariable Long productId)
	{
		return inventoryService.getStock(productId);
	}
	
	@Operation(summary="Reduce Stock for a product",description="Reduce Stock quantity currently present for a given product")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="Reduced Stock for a given product"),
			@ApiResponse(responseCode="500",description="Internal Server Error"),
			@ApiResponse(responseCode="404",description="Inventory Not Found for given product"),
			@ApiResponse(responseCode="400",description="Insufficient Stock for a given product")
	})
	@PostMapping("/reduce")
	public String reduceStock(@Valid @RequestBody ReduceStockRequest request)
	{
		return inventoryService.reduceStock(request);
	}

}
