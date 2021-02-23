package com.xxbb.simplemvc.http.server;

import com.xxbb.simplemvc.http.HttpHeaders;
import com.xxbb.simplemvc.http.HttpMethod;
import com.xxbb.simplemvc.http.InvalidMediaTypeException;
import com.xxbb.simplemvc.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

public class ServletServerHttpRequest implements ServerHttpRequest {

    protected static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";

    protected static final Charset FORM_CHARSET = StandardCharsets.UTF_8;

    private final HttpServletRequest servletRequest;

    @Nullable
    private URI uri;

    @Nullable
    private HttpHeaders headers;


    public ServletServerHttpRequest(HttpServletRequest servletRequest) {
        Assert.notNull(servletRequest, "Httpservlet must not be null");
        this.servletRequest = servletRequest;
    }

    public HttpServletRequest getServletRequest() {
        return this.servletRequest;
    }

    @Override
    @Nullable
    public HttpMethod getMethod() {
        return HttpMethod.resolve(this.servletRequest.getMethod());
    }

    @Override
    public String getMethodValue() {
        return this.servletRequest.getMethod();
    }

    @Override
    public Principal getPrincipal() {
        return this.servletRequest.getUserPrincipal();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return new InetSocketAddress(this.servletRequest.getLocalName(), this.servletRequest.getLocalPort());
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return new InetSocketAddress(this.servletRequest.getRemoteAddr(), this.servletRequest.getRemotePort());
    }

    @Override
    public InputStream getBody() throws IOException {
        if (isFormPost(this.servletRequest)) {
            return getBodyFromServletRequestParamters(this.servletRequest);
        } else {
            return this.servletRequest.getInputStream();
        }
    }

    @Override
    public URI getURI() {
        if (this.uri == null) {
            String urlString = null;
            boolean hasQuery = false;
            try {
                StringBuffer url = this.servletRequest.getRequestURL();
                String query = this.servletRequest.getQueryString();
                hasQuery = StringUtils.hasText(query);
                if (hasQuery) {
                    url.append("?").append(query);
                }
                urlString = url.toString();
                this.uri = new URI(urlString);
            } catch (URISyntaxException ex) {
                if (!hasQuery) {
                    throw new IllegalStateException("Could not resolve HttpServletRequest as URI: " + urlString, ex);
                }

                try {
                    urlString = this.servletRequest.getRequestURL().toString();
                    this.uri = new URI(urlString);
                } catch (URISyntaxException ex2) {
                    throw new IllegalStateException("Could not resolve HttpServletRequest as URI: " + urlString, ex2);
                }
            }
        }
        return this.uri;
    }

    @Override
    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            this.headers = new HttpHeaders();

            for (Enumeration<?> names = this.servletRequest.getHeaderNames(); names.hasMoreElements();) {
                String headerName = (String) names.nextElement();
                for (Enumeration<?> headerValues = this.servletRequest.getHeaders(headerName); headerValues.hasMoreElements();) {
                    String headerValue = (String) headerValues.nextElement();
                    this.headers.add(headerName, headerValue);
                }
            }
            try {
                MediaType contentType = this.headers.getContentType();
                if (contentType == null) {
                    String requestContentType = this.servletRequest.getContentType();
                    if (StringUtils.hasLength(requestContentType)) {
                        contentType = MediaType.parseMediaType(requestContentType);
                        this.headers.setContentType(contentType);
                    }
                }

                if (contentType != null && contentType.getCharset() == null) {
                    String requestEncoding = this.servletRequest.getCharacterEncoding();
                    if (StringUtils.hasLength(requestEncoding)) {
                        Charset charset = Charset.forName(requestEncoding);
                        Map<String, String> params = new LinkedCaseInsensitiveMap<>();
                        params.putAll(contentType.getParameters());
                        params.put("charset", charset.toString());
                        MediaType mediaType = new MediaType(contentType.getType(), contentType.getSubtype(), params);
                        this.headers.setContentType(mediaType);
                    }
                }
            } catch (InvalidMediaTypeException ignored) {
            }

            if (this.headers.getContentLength() < 0) {
                int requestContentLength = this.servletRequest.getContentLength();
                if (requestContentLength != -1) {
                    this.headers.setContentLength(requestContentLength);
                }
            }
        }
        return this.headers;
    }

    private static boolean isFormPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.contains(FORM_CONTENT_TYPE) && HttpMethod.POST.matches(request.getMethod());
    }

    private static InputStream getBodyFromServletRequestParamters(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        Writer writer = new OutputStreamWriter(bos, FORM_CHARSET);

        Map<String, String[]> form = request.getParameterMap();
        for(Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext();) {
            String name = nameIterator.next();
            List<String> values = Arrays.asList(form.get(name));
            for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext();) {
                String value = valueIterator.next();
                writer.write(URLEncoder.encode(name, FORM_CHARSET.name()));
                if (value != null) {
                    writer.write('=');
                    writer.write(URLEncoder.encode(value, FORM_CHARSET.name()));
                    if (valueIterator.hasNext()) {
                        writer.write('&');
                    }
                }
            }
            if (nameIterator.hasNext()) {
                writer.append('&');
            }
        }
        writer.flush();
        return new ByteArrayInputStream(bos.toByteArray());
    }
}
