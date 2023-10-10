package com.lotzy.skcrew.spigot.requests;

import ch.njol.util.Pair;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;

public class RequestUtil {

    static public Pair<Integer, String> makeSyncRequest(String method,String url, RequestProperty[] headers, String data) throws IOException, InterruptedException, URISyntaxException {
        url = parseParams(url);
        URI uri = new URI(url);
        
        HttpUriRequestBase request = new HttpUriRequestBase(method.toUpperCase(),uri);
        if (data != null)
            request.setEntity(new StringEntity(data));
        if (headers != null)
            for (RequestProperty property : headers) request.addHeader(property.key, property.value);    
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(request);
  
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity!=null) {
            try {
                result = EntityUtils.toString(entity);
            } catch (ParseException ex) {}
        }

        return new Pair(response.getCode(),result);
    }

    static public CompletableFuture<SimpleHttpResponse> makeAsyncRequest(String method,String url, RequestProperty[] headers, String data) throws IOException, InterruptedException, URISyntaxException, ExecutionException {
        url = parseParams(url);
        URI uri = new URI(url);
        
        SimpleHttpRequest request = new SimpleHttpRequest(method.toUpperCase(),uri);
        
        if (data != null) {
            StringEntity ent = new StringEntity(data);
            request.setBody(data, ContentType.parse(ent.getContentType()));
        }
        if (headers != null)
            for (RequestProperty property : headers) request.addHeader(property.key, property.value);    
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createHttp2Default();
        httpClient.start();
        CompletableFuture<SimpleHttpResponse> promise = new CompletableFuture<>();
        httpClient.execute(request,new FutureCallback<SimpleHttpResponse>() { 
            @Override
            public void completed(SimpleHttpResponse response) {
                promise.complete(response);
            }
            @Override
            public void failed(Exception ex) {
                promise.completeExceptionally(ex);
            }
            @Override
            public void cancelled() {
                promise.cancel(true);
            }      
        });
        return promise;
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
