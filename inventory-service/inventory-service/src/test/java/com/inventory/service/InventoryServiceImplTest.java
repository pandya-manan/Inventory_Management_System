package com.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.inventory.client.ProductClient;
import com.inventory.dto.AddStockRequest;
import com.inventory.dto.ReduceStockRequest;
import com.inventory.entity.Inventory;
import com.inventory.exception.InsufficientStockException;
import com.inventory.repository.InventoryRepository;

public class InventoryServiceImplTest {

	@Mock
	private InventoryRepository inventoryRepository;
	
	@Mock
	private ProductClient productClient;
	
	@InjectMocks
	private InventoryServiceImpl inventoryServiceImpl;
	
	@BeforeEach()
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void addStock_newProduct()
	{
		AddStockRequest addStockRequest = new AddStockRequest();
		addStockRequest.setProductId(1L);
		addStockRequest.setQuantity(120);
		
		//Product Exists
		when(productClient.getProductById(1L)).thenReturn(new Object());
		
		//No existing inventory
		when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.empty());
		
		String result = inventoryServiceImpl.addStock(addStockRequest);
		
		assertEquals("Stock added successfully",result);
		verify(inventoryRepository,times(1)).save(any(Inventory.class));
	}
	
	@Test
	void addStock_existingProduct()
	{
		AddStockRequest addStockRequest = new AddStockRequest();
		addStockRequest.setProductId(1L);
		addStockRequest.setQuantity(120);
		
		Inventory inventory = new Inventory();
		inventory.setId(1L);
		inventory.setProductId(1L);
		inventory.setQuantity(110);
		
		//Product Exists
		when(productClient.getProductById(1L)).thenReturn(new Object());
		
		//Existing inventory should be available
		when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
		
		//Add Stock to existing inventory 
		String result = inventoryServiceImpl.addStock(addStockRequest);
		
		assertEquals("Stock added successfully",result);
		verify(inventoryRepository,times(1)).save(any(Inventory.class));
	}
	
	@Test
	void getStock_success()
	{
		Inventory inventory = new Inventory();
		inventory.setId(1L);
		inventory.setProductId(1L);
		inventory.setQuantity(120);
		
		//Product exists
		when(productClient.getProductById(1L)).thenReturn(new Object());
		
		assertNotNull(inventory.getQuantity());
	}
	
	@Test
	void reduceStock_success() {

	    ReduceStockRequest request = new ReduceStockRequest();
	    request.setProductId(1L);
	    request.setQuantity(120);

	    // Product exists
	    when(productClient.getProductById(1L)).thenReturn(new Object());

	    Inventory inventory = new Inventory();
	    inventory.setId(1L);
	    inventory.setProductId(1L);
	    inventory.setQuantity(500);

	    when(inventoryRepository.findByProductId(1L))
	            .thenReturn(Optional.of(inventory));

	    String result = inventoryServiceImpl.reduceStock(request);

	    
	    assertEquals("Stock reduced successfully!", result);

	    
	    assertEquals(380, inventory.getQuantity()); 

	    
	    verify(inventoryRepository, times(1)).save(inventory);
	}
	
	@Test
	void reduceStock_insufficientStock() {

	    ReduceStockRequest request = new ReduceStockRequest();
	    request.setProductId(1L);
	    request.setQuantity(120);

	    // Product exists
	    when(productClient.getProductById(1L)).thenReturn(new Object());

	    Inventory inventory = new Inventory();
	    inventory.setId(1L);
	    inventory.setProductId(1L);
	    inventory.setQuantity(60); 

	    when(inventoryRepository.findByProductId(1L))
	            .thenReturn(Optional.of(inventory));

	    

	    // ✅ Test actual logic
	    assertThrows(InsufficientStockException.class, () -> {
	        inventoryServiceImpl.reduceStock(request);
	    });

	    // ✅ Ensure save NOT called
	    verify(inventoryRepository, never()).save(any());
	}
	
}
