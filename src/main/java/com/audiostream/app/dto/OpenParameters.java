package com.audiostream.app.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class OpenParameters {
	private String organizationId;
	private String conversationId;
	private Participant participant;
	private List<MediaParameter> media;
	private String language;
	private Boolean supportedLanguages;
	private Object customConfig;
	private Map<String, String> inputVariables;
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public Participant getParticipant() {
		return participant;
	}
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	public List<MediaParameter> getMedia() {
		return media;
	}
	public void setMedia(List<MediaParameter> media) {
		this.media = media;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Boolean getSupportedLanguages() {
		return supportedLanguages;
	}
	public void setSupportedLanguages(Boolean supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}
	public Object getCustomConfig() {
		return customConfig;
	}
	public void setCustomConfig(Object customConfig) {
		this.customConfig = customConfig;
	}
	public Map<String, String> getInputVariables() {
		return inputVariables;
	}
	public void setInputVariables(Map<String, String> inputVariables) {
		this.inputVariables = inputVariables;
	}
	
}
