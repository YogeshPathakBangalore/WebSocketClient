package com.audiostream.app.helper;

import java.util.function.Consumer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MicrophoneAudioCapture {
	private static TargetDataLine microphoneLine;
	private static final Logger log = LoggerFactory.getLogger(MicrophoneAudioCapture.class);
	public void startCapturing(int sampleRate, Consumer<byte[]> onAudioChunk) throws LineUnavailableException {
		AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		microphoneLine = (TargetDataLine) AudioSystem.getLine(info);
		microphoneLine.open(format);
		microphoneLine.start();

		log.info("🎤 Microphone started successfully.");

		// Run capture in a separate thread
		new Thread(() -> {
			byte[] buffer = new byte[4096]; // ~250ms buffer
			try {
				while (!Thread.currentThread().isInterrupted() && microphoneLine.isOpen()) {
					int bytesRead = microphoneLine.read(buffer, 0, buffer.length);
					if (bytesRead > 0) {
						byte[] chunk = new byte[bytesRead];
						System.arraycopy(buffer, 0, chunk, 0, bytesRead);
						onAudioChunk.accept(chunk); // send chunk in real-time
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				stop();
			}
		}, "Mic-Capture-Thread").start();
	}

	public void stop() {
		if (microphoneLine != null) {
			microphoneLine.stop();
			microphoneLine.close();
			log.info("🎙️ Microphone stopped and closed.");
		}
	}
}
