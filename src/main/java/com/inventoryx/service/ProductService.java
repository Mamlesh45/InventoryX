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
	
	
	public Product updateProduct(Long id , Product updatedProduct) {
		Product product = productRepository.findById(id).orElse(null);
		if(product == null) {
			return null;
		}
		product.setName(updatedProduct.getName());
		product.setDescription(updatedProduct.getDescription());
		product.setSku(updatedProduct.getSku());
		product.setPrice(updatedProduct.getPrice());
		product.setQuantity(updatedProduct.getQuantity());
		
		return productRepository.save(product);
	}
	
	
	public String deleteProduct(Long id) {
		Product product = productRepository.findById(id).orElse(null);
		
		if(product == null) {
			return "Product Not Found";
		}
		productRepository.delete(product);
		
		return "Product Deleted Successfully";
	}
	
}
