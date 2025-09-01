package com.audiostream.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OpenedMessage {
	private String version;
	private String id;
	private String type;
	private Integer seq;
	private Integer clientseq;
	private OpenedParameters parameters;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getClientseq() {
		return clientseq;
	}

	public void setClientseq(Integer clientseq) {
		this.clientseq = clientseq;
	}

	public OpenedParameters getParameters() {
		return parameters;
	}

	public void setParameters(OpenedParameters parameters) {
		this.parameters = parameters;
	}

}
