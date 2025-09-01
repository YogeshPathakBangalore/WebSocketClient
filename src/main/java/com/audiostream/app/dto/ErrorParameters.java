package com.audiostream.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorParameters {
	private Integer code;
	private String message;
	private String retryAfter; // optional
}
