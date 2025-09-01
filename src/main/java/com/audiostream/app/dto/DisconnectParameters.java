package com.audiostream.app.dto;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DisconnectParameters {
	private String reason; // "completed", "unauthorized", "error"
	private String info; // optional
	private Map<String, String> outputVariables; // optional

}
