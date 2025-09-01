package com.audiostream.app.client;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;

import com.audiostream.app.dto.CloseMessage;
import com.audiostream.app.dto.ClosedMessage;
import com.audiostream.app.dto.OpenMessage;
import com.audiostream.app.dto.OpenedMessage;
import com.audiostream.app.dto.PauseMessage;
import com.audiostream.app.dto.PausedMessage;
import com.audiostream.app.dto.ResumeMessage;
import com.audiostream.app.dto.ResumedMessage;
import com.audiostream.app.dto.UpdateMessage;
import com.audiostream.app.service.ClientTranscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ComponentScan
public class AudioWebSocketClient extends WebSocketClient {

	private static final Logger log = LoggerFactory.getLogger(AudioWebSocketClient.class);
	private final String sessionId;
	private final String participantId;
	private final CountDownLatch openedLatch = new CountDownLatch(1);
	private OpenedMessage openedMessageFromServer;
	private CloseMessage closeMessageFromClient;
	private CloseMessage closeMessageFromServer;
	private ClosedMessage closedMessageFromClient;
	private ClosedMessage closedMessageFromServer;
	private PauseMessage pauseMessageFromServer;
	private PauseMessage pauseMessageFromClient;
	private PausedMessage pausedMessageFromClient;
	private PausedMessage pausedMessageFromServer;
	private ResumeMessage resumeMessageFromClient;
	private ResumeMessage resumeMessageFromServer;
	private ResumedMessage resumedMessageFromClient;
	private ResumedMessage resumedMessageFromServer;
	private UpdateMessage updateMessageFromServer;
	private UpdateMessage updateMessageFromClient;
	ObjectMapper mapper = new ObjectMapper();

	
	public AudioWebSocketClient(String serverUri, String sessionId, String participantId, Map<String, String> headers) throws Exception {
		super(new URI(serverUri), headers);
		this.sessionId = sessionId;
		this.participantId = participantId;
	}

	@Override
	public void onOpen(ServerHandshake handshake) {
		log.info("🔌 Connected to WebSocket");

		OpenMessage openMessage = null;
		try {

			ObjectMapper mapper = new ObjectMapper();
			ClassPathResource resource = new ClassPathResource("openMessage.json");
			openMessage = mapper.readValue(resource.getInputStream(), OpenMessage.class);
			String jsonString = mapper.writeValueAsString(openMessage);

			log.info("Sending JSON: " + jsonString);

			// ✅ Send valid JSON over WebSocket
			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onMessage(String message) {
	    try {
	        log.debug("RAW: {}", message);  // always log raw server message

	        JSONObject response = new JSONObject(message);
	        String type = response.optString("type", null);

	        if (type == null || type.isBlank()) {
	            log.warn("⚠️ Received message without type: {}", message);
	            return;
	        }

	        log.info("📩 Received type: {}", type);

	        switch (type) {
	            case "opened"  -> handleOpened(message);
	            case "pause"   -> handlePause(message);
	            case "paused"   -> handlePaused(message);
	            case "pong"    -> handlePong(message);
	            case "resumed"   -> handleResumed(message);
	            case "close"   -> handleClose(message);
	            case "closed"  -> handleClosed(message);
	            default        -> log.warn("⚠️ Unknown message type: {}, raw: {}", type, message);
	        }

	    } catch (Exception e) {
	        log.error("❌ Error parsing server message: {}", message, e);
	    }
	}

	private void handleResumed(String message) {
		try {
			log.info("Received Resumed Event from Server");
			ObjectMapper mapper = new ObjectMapper();
			resumedMessageFromServer = mapper.readValue(message, ResumedMessage.class);
			log.info(mapper.writeValueAsString(resumedMessageFromServer));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void handlePaused(String message) {
		try {
			log.info("Received Paused Event from Server");
			ObjectMapper mapper = new ObjectMapper();
			pausedMessageFromServer = mapper.readValue(message, PausedMessage.class);
			log.info(mapper.writeValueAsString(pausedMessageFromServer));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void handleClose(String message) {
		try {
			log.info("Received Close Event from Server");
			ObjectMapper mapper = new ObjectMapper();
			closeMessageFromServer = mapper.readValue(message, CloseMessage.class);
			log.info(mapper.writeValueAsString(closeMessageFromServer));
			// Need to stop Mic and Websocket
			
			closedMessageFromClient = new ClosedMessage();
			closedMessageFromClient.setType("closed");
			closedMessageFromClient.setVersion(closeMessageFromServer.getVersion());
			closedMessageFromClient.setServerseq(closeMessageFromServer.getSeq());
			closedMessageFromClient.setSeq(closeMessageFromServer.getClientseq() + 1);
			closedMessageFromClient.setId(closeMessageFromServer.getId());
			
			String jsonString = mapper.writeValueAsString(closedMessageFromClient);
			System.out.println("Sending Closed JSON: " + jsonString);
			
			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlePause(String message) {
		try {
			log.info("Received Pause Event from Server");
			ObjectMapper mapper = new ObjectMapper();
			pauseMessageFromServer = mapper.readValue(message, PauseMessage.class);
			log.info(mapper.writeValueAsString(pauseMessageFromServer));
			
			//Need to send Paused Event to server
			pausedMessageFromClient = new PausedMessage();
			pausedMessageFromClient.setId(pauseMessageFromServer.getId());
			pausedMessageFromClient.setVersion(pauseMessageFromServer.getVersion());
			pausedMessageFromClient.setPosition("");
			pausedMessageFromClient.setSeq(pauseMessageFromServer.getClientseq()+1);
			pausedMessageFromClient.setServerseq(pauseMessageFromServer.getSeq());
			pausedMessageFromClient.setType("paused");
			
			String jsonString = mapper.writeValueAsString(pausedMessageFromClient);
			System.out.println("Sending Paused JSON: " + jsonString);
			
			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleClosed(String message) {
		try {
			log.info("Received Closed Event from Server");
			ObjectMapper mapper = new ObjectMapper();
			closedMessageFromServer = mapper.readValue(message, ClosedMessage.class);
			log.info(mapper.writeValueAsString(closedMessageFromServer));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private Object handlePong(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	private void handleOpened(String message) {
		try {
			log.info("✅ Stream opened by server.");
			openedMessageFromServer = mapper.readValue(message, OpenedMessage.class);
			log.info(mapper.writeValueAsString(openedMessageFromServer));
			openedLatch.countDown(); // allow audio to start
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		log.info("🔌 WebSocket connection closed: " + reason);
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("❌ WebSocket error: " + ex.getMessage());
		ex.printStackTrace();
	}

	// Send audio only after "opened"
	public void sendAudio(byte[] audioBytes) throws InterruptedException {
		openedLatch.await(); // wait for "opened" before sending
		send(ByteBuffer.wrap(audioBytes)); // send as binary
	}

	public void sendCloseEvent() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ClassPathResource resource = new ClassPathResource("closeMessage.json");
			closeMessageFromClient = mapper.readValue(resource.getInputStream(), CloseMessage.class);
			String jsonString = mapper.writeValueAsString(closeMessageFromClient);
			log.info("Sending Close JSON: " + jsonString);
			// ✅ Send valid JSON over WebSocket
			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendPauseEventToServer() {
		ClassPathResource resource = new ClassPathResource("pauseMessage.json");
		try {
			pauseMessageFromClient = mapper.readValue(resource.getInputStream(), PauseMessage.class);
			String jsonString = mapper.writeValueAsString(pauseMessageFromClient);

			log.info("Sending Pause JSON: " + jsonString);

			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendResumeEventToServer() {
		ClassPathResource resource = new ClassPathResource("resumeMessage.json");
		try {
			resumeMessageFromClient = mapper.readValue(resource.getInputStream(), ResumeMessage.class);
			String jsonString = mapper.writeValueAsString(resumeMessageFromClient);

			log.info("Sending Resume JSON: " + jsonString);

			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendUpdateEventToServer() {
		ClassPathResource resource = new ClassPathResource("updateMessage.json");
		try {
			updateMessageFromClient = mapper.readValue(resource.getInputStream(), UpdateMessage.class);
			String jsonString = mapper.writeValueAsString(updateMessageFromClient);

			log.info("Sending Update JSON: " + jsonString);

			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void sendPausedEventToServer() {
		ClassPathResource resource = new ClassPathResource("pausedMessage.json");
		try {
			pausedMessageFromClient = mapper.readValue(resource.getInputStream(), PausedMessage.class);
			String jsonString = mapper.writeValueAsString(pausedMessageFromClient);

			log.info("Sending Paused JSON: " + jsonString);

			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void sendClosedEventToServer() {
		ClassPathResource resource = new ClassPathResource("closedMessage.json");
		try {
			closedMessageFromClient = mapper.readValue(resource.getInputStream(), ClosedMessage.class);
			String jsonString = mapper.writeValueAsString(closedMessageFromClient);

			log.info("Sending Closed JSON: " + jsonString);

			send(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
