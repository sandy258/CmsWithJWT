package com.user.exception.handler;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse {
	private String status;
	private int code;
	private String message;
	private HttpStatus httpStatus;
}
