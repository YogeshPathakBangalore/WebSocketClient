package com.audiostream.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PingMessage {
	private String version;
	private String id;
	private String type;
	private Integer seq;
	private Integer serverseq;
	private String position;
	private PingParameters parameters;
}
