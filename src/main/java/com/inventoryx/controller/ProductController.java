package com.inventoryx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import com.inventoryx.dto.ProductRequestDTO;
import com.inventoryx.entity.Product;
import com.inventoryx.payload.ApiResponse;
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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Product>> save(
	        @Valid
	        @RequestBody ProductRequestDTO dto){

	    Product product = productService.saveProduct(dto);

	    ApiResponse<Product> response =
	            new ApiResponse<>(

	                    true,

	                    HttpStatus.CREATED.value(),

	                    "Product created successfully",

	                    product
	            );

	    return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(){

	    List<Product> products = productService.getAllProducts();

	    ApiResponse<List<Product>> response =
	            new ApiResponse<>(

	                    true,

	                    HttpStatus.OK.value(),

	                    "Products fetched successfully",

	                    products
	            );

	    return ResponseEntity.ok(response);
	}
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> getProductById(
			@PathVariable Long id){
		Product product = productService.getProductById(id);

		ApiResponse<Product> response = new ApiResponse<>(
				true,
				HttpStatus.OK.value(),
				"product fetched successfully",
				product
				);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Product>> updateProduct(
	        @PathVariable Long id,
	        @Valid @RequestBody ProductRequestDTO dto) {

	    Product updatedProduct =
	            productService.updateProduct(id, dto);

	    ApiResponse<Product> response =
	            new ApiResponse<>(
	                    true,
	                    HttpStatus.OK.value(),
	                    "Product updated successfully",
	                    updatedProduct
	            );

	    return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<String>> deleteProduct(
	        @PathVariable Long id){

	    String message = productService.deleteProduct(id);

	    ApiResponse<String> response =
	            new ApiResponse<>(

	                    true,

	                    HttpStatus.OK.value(),

	                    message,

	                    null
	            );

	    return ResponseEntity.ok(response);
	}
}
