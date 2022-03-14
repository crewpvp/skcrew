package com.lotzy.skcrew.map.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class HttpUtils {

    @SuppressWarnings("deprecation")
    private static final JsonParser JSON_PARSER = new JsonParser();

    @SuppressWarnings("deprecation")
    public static JsonElement parseJson(Reader json) {
        return JSON_PARSER.parse(json);
    }

    public static JsonElement parseJson(String json) {
        return parseJson(new StringReader(json));
    }

    public static JsonElement parseJson(byte[] json) {
        return parseJson(new InputStreamReader(new ByteArrayInputStream(json)));
    }




    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static <T> Optional<HttpResponse<T>> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        try {
            return Optional.of(CLIENT.send(request, bodyHandler));
        } catch (Exception ignore) {
            return Optional.empty();
        }
    }

    public static <T> Optional<HttpResponse<T>> getRequest(String uri, HttpResponse.BodyHandler<T> bodyHandler) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();
        return send(request, bodyHandler);
    }

    public static <T> Optional<T> getBody(String uri, HttpResponse.BodyHandler<T> bodyHandler) {
        return getRequest(uri, bodyHandler).map(HttpResponse::body);
    }

    public static Optional<String> simpleGetRequest(String uri) {
        return getBody(uri, HttpResponse.BodyHandlers.ofString());
    }

}
