package com.inventoryx.service;

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

	    System.out.println(product.getName());
	    System.out.println(product.getDescription());
	    System.out.println(product.getSku());
	    System.out.println(product.getPrice());
	    System.out.println(product.getQuantity());

	    return productRepository.save(product);
	}
}
