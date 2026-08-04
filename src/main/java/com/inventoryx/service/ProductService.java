package com.inventoryx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventoryx.entity.Product;
import com.inventoryx.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public Product saveProduct(Product product) {
	    return productRepository.save(product);
	}
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
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
