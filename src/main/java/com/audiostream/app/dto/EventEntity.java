package com.audiostream.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventEntity {
	private String type;
	private Object data;
}
