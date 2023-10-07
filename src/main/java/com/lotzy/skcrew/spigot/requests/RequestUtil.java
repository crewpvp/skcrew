package com.lotzy.skcrew.spigot.requests;

import ch.njol.util.Pair;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;

public class RequestUtil {
    @Nullable
    static public Pair<Integer, String> makeSyncRequest(String method,String url, @Nullable RequestProperty[] headers, @Nullable String data) throws IOException, InterruptedException, URISyntaxException {
        url = parseParams(url);
        Builder request = HttpRequest.newBuilder(new URI(url));
        if (data != null) {
            request.method(method, HttpRequest.BodyPublishers.ofString(data));
        } else {
            request.method(method, HttpRequest.BodyPublishers.noBody());
        }
        if (headers != null)
            for (RequestProperty property : headers) request.header(property.key, property.value);    
        HttpRequest buildedRequest = request.build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(buildedRequest, HttpResponse.BodyHandlers.ofString());
        return new Pair(response.statusCode(),response.body());
    }
    @Nullable
    static public CompletableFuture<HttpResponse<String>> makeAsyncRequest(String method,String url, @Nullable RequestProperty[] headers, @Nullable String data) throws IOException, InterruptedException, URISyntaxException {
        url = parseParams(url);
        Builder request = HttpRequest.newBuilder(new URI(url));
        if (data != null) {
            request.method(method, HttpRequest.BodyPublishers.ofString(data));
        } else {
            request.method(method, HttpRequest.BodyPublishers.noBody());
        }
        if (headers != null)
            for (RequestProperty property : headers) request.header(property.key, property.value);
        HttpRequest buildedRequest = request.build();
        return HttpClient.newHttpClient().sendAsync(buildedRequest, HttpResponse.BodyHandlers.ofString());
    }
    static public String parseParams(String url) {
        String[] parts = url.split("\\?", 1);
        if (parts.length > 1)
            try {
                url = parts[0]+"?"+URLEncoder.encode(parts[1], "UTF_8");
            } catch (UnsupportedEncodingException ex) {
                return "";
            }
        return url;
    }
}
