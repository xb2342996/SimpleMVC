package com.xxbb.simplemvc.http.server;

import com.xxbb.simplemvc.http.HttpInputMessage;
import com.xxbb.simplemvc.http.HttpRequest;
import org.springframework.lang.Nullable;

import java.net.InetSocketAddress;
import java.security.Principal;

public interface ServerHttpRequest extends HttpRequest, HttpInputMessage {
    @Nullable
    Principal getPrincipal();

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

}
