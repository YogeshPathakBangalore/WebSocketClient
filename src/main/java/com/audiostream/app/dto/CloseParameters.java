package com.audiostream.app.dto;

public class CloseParameters {
	private String reason; // "end", "disconnect", "error"

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
