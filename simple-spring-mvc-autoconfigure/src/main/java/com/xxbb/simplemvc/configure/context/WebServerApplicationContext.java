package com.xxbb.simplemvc.configure.context;

import org.springframework.boot.web.server.WebServer;

public interface WebServerApplicationContext {
    WebServer getWebServer();
}
