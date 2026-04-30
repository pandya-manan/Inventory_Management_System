package com.inventory.product.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inventory.product.dto.ProductRequest;
import com.inventory.product.entity.Product;
import com.inventory.product.exception.ProductNotFoundException;
import com.inventory.product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepo = productRepository;
    }

    // ==============================
    // ADD PRODUCT
    // ==============================
    @Override
    public String addProduct(ProductRequest productRequest) {

        log.info("Adding product: name={}, price={}, sku={}, category={}",
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getSku(),
                productRequest.getCategory());

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setSku(productRequest.getSku());
        product.setCategory(productRequest.getCategory());

        productRepo.save(product);

        log.info("Product saved successfully: productId={}, name={}",
                product.getId(), product.getName());

        return "Product has been saved successfully";
    }

    // ==============================
    // GET ALL PRODUCTS
    // ==============================
    @Override
    public List<Product> getProducts() {
        log.info("Fetching all products");
        return productRepo.findAll();
    }

    // ==============================
    // GET PRODUCT BY ID
    // ==============================
    @Override
    public Product getProductById(Long productId) {

        log.info("Fetching product: productId={}", productId);

        return productRepo.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found: productId={}", productId);
                    return new ProductNotFoundException("Product not found for the given product id");
                });
    }

    // ==============================
    // UPDATE PRODUCT
    // ==============================
    @Override
    public String updateProduct(Long id, ProductRequest request) {

        log.info("Updating product: productId={}", id);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found for update: productId={}", id);
                    return new ProductNotFoundException("Product not found for the given product id");
                });

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setSku(request.getSku());

        productRepo.save(product);

        log.info("Product updated successfully: productId={}", id);

        return "Product Updated Successfully";
    }

    // ==============================
    // DELETE PRODUCT
    // ==============================
    @Override
    public String deleteProduct(Long id) {

        log.info("Deleting product: productId={}", id);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found for deletion: productId={}", id);
                    return new ProductNotFoundException("Product not found for the given product id");
                });

        productRepo.deleteById(id);

        log.info("Product deleted successfully: productId={}", id);

        return "Product Deleted Successfully";
    }
}