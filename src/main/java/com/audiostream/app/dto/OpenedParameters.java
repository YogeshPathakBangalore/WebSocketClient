package com.audiostream.app.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OpenedParameters {
	private List<MediaParameter> media;
	private String discardTo; // ISO duration, optional
	private Boolean startPaused; // optional
	private List<String> supportedLanguages; // optional

	public List<MediaParameter> getMedia() {
		return media;
	}

	public void setMedia(List<MediaParameter> media) {
		this.media = media;
	}

	public String getDiscardTo() {
		return discardTo;
	}

	public void setDiscardTo(String discardTo) {
		this.discardTo = discardTo;
	}

	public Boolean getStartPaused() {
		return startPaused;
	}

	public void setStartPaused(Boolean startPaused) {
		this.startPaused = startPaused;
	}

	public List<String> getSupportedLanguages() {
		return supportedLanguages;
	}

	public void setSupportedLanguages(List<String> supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}

}
