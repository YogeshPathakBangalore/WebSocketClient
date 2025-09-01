package com.audiostream.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiscardedParameters {
	private String start;
	private String discarded;
}
