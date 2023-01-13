package com.lotzy.skcrew.requests;

import ch.njol.util.Pair;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;

public class RequestUtil {
    static public Pair<Integer, String> makeSyncRequest(String method,String url, @Nullable RequestProperty[] headers, @Nullable String data) throws IOException, InterruptedException {
        url = URLEncoder.encode(url, "UTF-8");
        Builder request = HttpRequest.newBuilder();
        request.uri(URI.create(url));
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
    static public CompletableFuture<HttpResponse<String>> makeAsyncRequest(String method,String url, @Nullable RequestProperty[] headers, @Nullable String data) throws IOException, InterruptedException {
        url = URLEncoder.encode(url, "UTF-8");
        Builder request = HttpRequest.newBuilder();
        request.uri(URI.create(url));
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
}
