package com.audiostream.app.dto;

import lombok.ToString;

@ToString
public class OpenMessage {
    private String version;
    private String id;
    private String type;
    private Integer seq;
    private Integer serverseq;
    private String position;
    private OpenParameters parameters;
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
	public OpenParameters getParameters() {
		return parameters;
	}
	public void setParameters(OpenParameters parameters) {
		this.parameters = parameters;
	}
    
}