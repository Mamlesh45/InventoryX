package com.inventoryx.payload;

import java.time.LocalDateTime;

public class ApiResponse <T> {

	private boolean success ; 
	private int status ;
	private String message;
	private LocalDateTime timestamp;
	private T data ;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ApiResponse() {
		
	}
	
	public ApiResponse(boolean success , int status , String message) {
		this.success = success ; 
		this.status = status ; 
		this.message = message ; 
		this.timestamp = LocalDateTime.now();
		}
	
	public ApiResponse(
	        boolean success,
	        int status,
	        String message,
	        T data
	) {
	    this.success = success;
	    this.status = status;
	    this.message = message;
	    this.data = data;
	    this.timestamp = LocalDateTime.now();
	}
	
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "ApiResponse [success=" + success + ", status=" + status + ", message=" + message + ", timestamp="
				+ timestamp + ", data=" + data + "]";
	}
	
	
	
}
