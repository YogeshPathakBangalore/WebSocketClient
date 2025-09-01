package com.audiostream.app.dto;

import java.util.List;

import lombok.ToString;
@ToString
public class MediaParameter {
	private String type; // "audio"
	private String format; // "PCMU"
	private List<String> channels; // "external", "internal"
	private Integer rate; // 8000
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public List<String> getChannels() {
		return channels;
	}
	public void setChannels(List<String> channels) {
		this.channels = channels;
	}
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	
}
