package com.audiostream.app.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.sampled.LineUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audiostream.app.client.AudioWebSocketClient;
import com.audiostream.app.helper.MicrophoneAudioCapture;

@Service
public class ClientTranscriptionService {

	private static final Logger logger = LoggerFactory.getLogger(ClientTranscriptionService.class);

	private AudioWebSocketClient wsClient;

	private final String sessionId = "demo-session-001";
	private final String participantId = "user-123";

	private MicrophoneAudioCapture mic;

	private static final String CLIENT_SECRET = "TXlTdXBlclNlY3JldEtleVRlbGxOby0xITJAMyM0JDU=";

	/**
	 * A helper method to compute the HMAC-SHA256 signature for the WebSocket
	 * handshake. This logic should match the server's validation logic exactly.
	 */
	private String computeSignature(Map<String, String> headers) throws NoSuchAlgorithmException, InvalidKeyException {
		// Construct the signing string in the canonical format.
		String signingString = "\"@request-target\": get /audiohook/ws\n" + "\"@authority\": localhost:8080\n"
				+ "\"audiohook-organization-id\": " + headers.get("Audiohook-Organization-Id") + "\n"
				+ "\"audiohook-correlation-id\": " + headers.get("Audiohook-Correlation-Id") + "\n"
				+ "\"audiohook-session-id\": " + headers.get("Audiohook-Session-Id") + "\n" + "\"x-api-key\": "
				+ headers.get("X-API-KEY") + "\n"
				+ "@signature-params: (\"@request-target\" \"@authority\" \"audiohook-organization-id\" \"audiohook-session-id\" \"audiohook-correlation-id\" \"x-api-key\")";

		Mac hmacSha256 = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKey = new SecretKeySpec(CLIENT_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSha256");
		hmacSha256.init(secretKey);

		byte[] hashBytes = hmacSha256.doFinal(signingString.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(hashBytes);
	}

	public synchronized void startStreamingToApi() {
		try {
			logger.info("🎤 Starting mic capture...");

			Map<String, String> headers = new HashMap<>();
			headers.put("Host", "audiohook.example.com");
			headers.put("Audiohook-Organization-Id", "d7934305-0972-4844-938e-9060eef73d05");
			headers.put("Audiohook-Correlation-Id", "e160e428-53e2-487c-977d-96989bf5c99d");
			headers.put("Audiohook-Session-Id", "30b0e395-84d3-4570-ac13-9a62d8f514c0");
			headers.put("X-API-KEY", "SGVsbG8sIEkgYW0gdGhlIEFQSSBrZXkh");
			headers.put("Signature", "sig1=:Nf+xItBPf2svtEWmjGnQkpnFlftlbyC9IZv5qX/smdw=:");
			headers.put("Signature-Input",
					"sig1=(\"@request-target\" \"@authority\" \"audiohook-organization-id\" \"audiohook-session-id\" \"audiohook-correlation-id\" \"x-api-key\");keyid=\"SGVsbG8sIEkgYW0gdGhlIEFQSSBrZXkh\";nonce=\"VGhpc0lzQVVuaXF1ZU5vbmNl\";alg=\"hmac-sha256\";created=1641013200;expires=3282026430");
			headers.put("participantId", participantId);
			wsClient = new AudioWebSocketClient("ws://localhost:8080/audiohook/ws", sessionId, participantId, headers);

			wsClient.connect();

			enableMic();

			logger.info("✅ Streaming started for session {}", sessionId);

		} catch (Exception e) {
			logger.error("❌ Error during startStreamingToApi: ", e);
		}
	}

	public void enableMic() throws LineUnavailableException {
		mic = new MicrophoneAudioCapture();
		mic.startCapturing(8000, audioChunk -> {
			try {
				wsClient.sendAudio(audioChunk);
			} catch (Exception e) {
				logger.error("❌ WebSocket send failed: ", e);
			}
		});
	}

	public synchronized void stopStreaming() {
		try {

			if (mic != null) {
				mic.stop();
				logger.info("🎤 Microphone stopped.");
			}

			if (wsClient != null && wsClient.isOpen()) {
				wsClient.sendCloseEvent(); // Inform server stream is done
				wsClient.close();
				logger.info("🔌 WebSocket closed for session {}", sessionId);
			}

			logger.info("🛑 Streaming session {} finished.", sessionId);

		} catch (Exception e) {
			logger.error("❌ Error during stopStreaming: ", e);
		}
	}

	public void pauseEvent() {
		logger.info("⏸️ Requesting pause...");
		pauseMic();
		wsClient.sendPauseEventToServer();
	}

	public synchronized void pauseMic() {
		if (mic != null) {
			logger.info("🎙️ Stopping microphone locally...");
			mic.stop();
			mic = null;
		} else {
			logger.warn("⚠️ pauseMic() called but mic is already null.");
		}
	}

	public void resumeEvent() {
		logger.info("▶️ Requesting resume...");
		wsClient.sendResumeEventToServer();
	}

	public void updateEvent() {
		wsClient.sendUpdateEventToServer();
		
	}

//	public void sendPausedEventToServer() {
//		pauseMic();
//		wsClient.sendPausedEventToServer();
//		
//	}
//
//	public void stopStreamingAndSendClosedEventToServer() {
//		stopStreaming();
//		wsClient.sendClosedEventToServer();
//		
//	}

}
