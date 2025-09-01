package com.audiostream.app.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventParameters {
	private List<EventEntity> entities;
}
