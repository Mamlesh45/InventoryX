package com.inventoryx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventoryx.dto.ProductRequestDTO;
import com.inventoryx.entity.Product;
import com.inventoryx.exception.ProductNotFoundException;
import com.inventoryx.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public Product saveProduct(ProductRequestDTO dto) {

	    Product product = new Product();

	    product.setName(dto.getName());
	    product.setSku(dto.getSku());
	    product.setPrice(dto.getPrice());
	    product.setQuantity(dto.getQuantity());

	    return productRepository.save(product);
	}
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public Product getProductById(Long id) {
		return productRepository
				.findById(id)
				.orElseThrow(() -> 
				     new ProductNotFoundException(
				    		 "product with ID " + id + " not found"
				    		 ));
	}
	
	
	public Product updateProduct(Long id, ProductRequestDTO dto) {

	    Product product = productRepository.findById(id)
	            .orElseThrow(() ->
	                    new ProductNotFoundException(
	                            "Product not found with id: " + id
	                    )
	            );

	    product.setName(dto.getName());
	    product.setDescription(dto.getDescription());
	    product.setSku(dto.getSku());
	    product.setPrice(dto.getPrice());
	    product.setQuantity(dto.getQuantity());

	    return productRepository.save(product);
	}
	
	
	public String deleteProduct(Long id) {

	    Product product = productRepository.findById(id)
	            .orElseThrow(() ->
	                    new ProductNotFoundException(
	                            "Product not found with id: " + id
	                    )
	            );

	    productRepository.delete(product);

	    return "Product deleted successfully";
	}
}
