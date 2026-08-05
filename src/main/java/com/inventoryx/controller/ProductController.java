package com.inventoryx.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventoryx.dto.ProductRequestDTO;
import com.inventoryx.entity.Product;
import com.inventoryx.service.ProductService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@PostMapping
	public Product save(@Valid @RequestBody ProductRequestDTO dto) {
	    return productService.saveProduct(dto);
	}
	@GetMapping
	public List<Product> getAllProducts(){
		return productService.getAllProducts();
	}
	
	@GetMapping("/{id}")
	public Product getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}
	
	@PutMapping("/{id}")
	public Product updateProduct(@PathVariable Long id , @RequestBody Product product) {
		return productService.updateProduct(id, product);
	}
	
	@DeleteMapping("/{id}")
	public String deleteProduct(@PathVariable Long id) {
		return productService.deleteProduct(id);
	}
}
