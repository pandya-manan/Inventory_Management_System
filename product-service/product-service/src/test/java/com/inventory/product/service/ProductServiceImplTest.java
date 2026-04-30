package com.inventory.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.inventory.product.dto.ProductRequest;
import com.inventory.product.entity.Product;
import com.inventory.product.exception.ProductNotFoundException;
import com.inventory.product.repository.ProductRepository;

public class ProductServiceImplTest {
	
	@Mock 
	private ProductRepository productRepo;
	
	@InjectMocks
	private ProductServiceImpl productService;
	
	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void addProduct_success() {

	    ProductRequest request = new ProductRequest();
	    request.setName("Rice");
	    request.setDescription("Good quality");
	    request.setPrice(BigDecimal.valueOf(100));
	    request.setSku("SKU123");
	    request.setCategory("Food");

	    String result = productService.addProduct(request);

	    assertEquals("Product has been saved successfully", result);

	    verify(productRepo, times(1)).save(any(Product.class));
	}
	
	@Test
	void getProducts_success() {

	    List<Product> products = List.of(new Product(), new Product());

	    when(productRepo.findAll()).thenReturn(products);

	    List<Product> result = productService.getProducts();

	    assertEquals(2, result.size());
	}

	@Test
	void getProductById_success() {

	    Product product = new Product();
	    product.setId(1L);

	    when(productRepo.findById(1L)).thenReturn(Optional.of(product));

	    Product result = productService.getProductById(1L);

	    assertEquals(1L, result.getId());
	}
	
	@Test
	void getProductById_notFound() {

	    when(productRepo.findById(1L)).thenReturn(Optional.empty());

	    assertThrows(ProductNotFoundException.class, () -> {
	        productService.getProductById(1L);
	    });
	}
	
	@Test
	void updateProduct_success() {

	    ProductRequest request = new ProductRequest();
	    request.setName("Updated");
	    request.setDescription("Updated desc");
	    request.setPrice(BigDecimal.valueOf(200));
	    request.setSku("SKU999");
	    request.setCategory("UpdatedCat");

	    Product product = new Product();
	    product.setId(1L);

	    when(productRepo.findById(1L)).thenReturn(Optional.of(product));

	    String result = productService.updateProduct(1L, request);

	    assertEquals("Product Updated Successfully", result);

	    // ✅ Verify fields updated
	    assertEquals("Updated", product.getName());
	    assertEquals("Updated desc", product.getDescription());

	    verify(productRepo).save(product);
	}
	
	@Test
	void updateProduct_notFound() {

	    ProductRequest request = new ProductRequest();

	    when(productRepo.findById(1L)).thenReturn(Optional.empty());

	    assertThrows(ProductNotFoundException.class, () -> {
	        productService.updateProduct(1L, request);
	    });
	}
	
	@Test
	void deleteProduct_success() {

	    Product product = new Product();
	    product.setId(1L);

	    when(productRepo.findById(1L)).thenReturn(Optional.of(product));

	    String result = productService.deleteProduct(1L);

	    assertEquals("Product Deleted Successfully", result);

	    verify(productRepo).deleteById(1L);
	}
	
	@Test
	void deleteProduct_notFound() {

	    when(productRepo.findById(1L)).thenReturn(Optional.empty());

	    assertThrows(ProductNotFoundException.class, () -> {
	        productService.deleteProduct(1L);
	    });
	}
}
