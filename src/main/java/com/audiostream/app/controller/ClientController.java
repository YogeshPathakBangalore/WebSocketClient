package com.audiostream.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.audiostream.app.service.ClientTranscriptionService;

@RestController
@RequestMapping("/client")
public class ClientController {

	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private ClientTranscriptionService transcriptionService;

	@GetMapping("/start")
	public String startStreaming() {
		logger.info("📨 Received request to start audio streaming from client.");
		transcriptionService.startStreamingToApi();
		return "🎙️ Microphone streaming started.";
	}

	@GetMapping("/stop")
	public String stopStreaming() {
		logger.info("📨 Received request to stop audio streaming from client.");
		transcriptionService.stopStreaming();
		return "🛑 Microphone streaming stopped.";
	}

	@GetMapping("/pause")
	public String pauseStreaming() {
		logger.info("📨 Received request to pause audio streaming from client.");
		transcriptionService.pauseEvent();
		return "🛑 Microphone streaming stopped.";
	}

	@GetMapping("/resume")
	public String resumeStreaming() {
		logger.info("📨 Received request to resume audio streaming from client.");
		try {
			transcriptionService.resumeEvent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "🎙️ Microphone streaming started.";
	}
	
	@GetMapping("/update")
	public String updateLanguage() {
		logger.info("📨 Received request to update Language from client.");
		transcriptionService.updateEvent();
		return "Send Update Request to Server";
	}
}
