package com.audiostream.app.service;

public interface WebSocketEventListener {
    void onResumed();
    void onPaused();
    void onClosed();
    void onError(Exception ex);
}
