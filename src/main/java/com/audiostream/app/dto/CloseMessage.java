package com.audiostream.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloseMessage {
	private String version;
	private String id;
	private String type;
	private Integer seq;
	private Integer serverseq;
	private Integer clientseq;
	private String position;
	private CloseParameters parameters;

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

	public Integer getServerseq() {
		return serverseq;
	}

	public void setServerseq(Integer serverseq) {
		this.serverseq = serverseq;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public CloseParameters getParameters() {
		return parameters;
	}

	public void setParameters(CloseParameters parameters) {
		this.parameters = parameters;
	}

}
