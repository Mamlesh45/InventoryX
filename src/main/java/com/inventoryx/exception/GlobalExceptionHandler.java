package com.inventoryx.exception;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.inventoryx.payload.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponse<String>> handleBadCredentials(
	        BadCredentialsException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    HttpStatus.UNAUTHORIZED.value(),
	                    "Invalid email or password",
	                    null
	            );

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.UNAUTHORIZED
	    );
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<String>> handleAccessDenied(
	        AccessDeniedException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    HttpStatus.FORBIDDEN.value(),
	                    "Access Denied",
	                    null
	            );

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.FORBIDDEN
	    );
	}
	
	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<ApiResponse<String>> handleSecurityException(
	        SecurityException ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    HttpStatus.FORBIDDEN.value(),
	                    ex.getMessage(),
	                    null
	            );

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.FORBIDDEN
	    );
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<String>> handleException(
	        Exception ex) {

	    ApiResponse<String> response =
	            new ApiResponse<>(
	                    false,
	                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                    "An unexpected error occurred",
	                    null
	            );

	    return new ResponseEntity<>(
	            response,
	            HttpStatus.INTERNAL_SERVER_ERROR
	    );
	}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        });

        ApiResponse<Map<String, String>> response =
                new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation failed",
                        errors
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

  
    
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleProductNotFound(
            ProductNotFoundException ex) {

        ApiResponse<String> response =
                new ApiResponse<>(
                        false,
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        null
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<String>> handleInsufficientStock(
            InsufficientStockException ex) {

        ApiResponse<String> response =
                new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        null
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(WarehouseNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleWarehouseNotFound(
            WarehouseNotFoundException ex) {

        ApiResponse<String> response =
                new ApiResponse<>(
                        false,
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        null
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
	}

}