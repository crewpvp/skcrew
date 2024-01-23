package com.lotzy.skcrew.proxy.sockets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.lotzy.skcrew.proxy.Skcrew;
import com.lotzy.skcrew.shared.sockets.data.BaseServer;
import com.lotzy.skcrew.shared.sockets.data.NetworkPlayer;
import com.lotzy.skcrew.shared.sockets.data.NetworkServer;
import com.lotzy.skcrew.shared.sockets.data.Signal;
import com.lotzy.skcrew.shared.sockets.packets.PacketOutcomingSignal;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WebServer implements Runnable {

    
    public enum Errors {
        WRONG_METHOD("{ \"type\": \"WRONG_METHOD\", \"message\": \"Web server supports only GET or POST methods of request\" }"),
        WRONG_PATH("{ \"type\": \"WRONG_PATH\", \"message\": \"Path, what you specified doesn't exists\" }"),
        WRONG_PARAMS("{ \"type\": \"WRONG_PARAMS\", \"message\": \"Some required params are missed\" }"),
        PLAYER_NOT_FOUND("{ \"type\": \"PLAYER_NOT_FOUND\", \"message\": \"Player, what you specified isn't online\" }"),
        SERVER_NOT_FOUND("{ \"type\": \"SERVER_NOT_FOUND\", \"message\": \"Server, what you specified isn't exists\" }"),
        PLAYER_NOT_SPECIFIED("{ \"type\": \"PLAYER_NOT_SPECIFIED\", \"message\": \"Specify name of player for fetch in your request\" }"),
        SERVER_NOT_SPECIFIED("{ \"type\": \"SERVER_NOT_SPECIFIED\", \"message\": \"Specify name of server for fetch in your request\" }"),
        SERVER_IS_OFFLINE("{ \"type\": \"SERVER_IS_OFFLINE\", \"message\": \"Server what you specified is offline\" }"),
        CONNECTION_CLOSED("{ \"type\": \"CONNECTION_CLOSED\", \"message\": \"Can't read request body\" }"),
        INVALID_JSON("{ \"type\": \"INVALID_JSON\", \"message\": \"Can't parse json\" }"),
        INVALID_SIGNAL_JSON("{ \"type\": \"INVALID_SIGNAL_JSON\", \"message\": \"Can't parse JSON to SIGNALS, check docs and fix request body\" }"),
        ;
        
        private final String response;
        private Errors(String response) {
            this.response = response;
        }
        public String getResponse() {
            return this.response;
        }
    }
    
    public String build_error(WebServer.Errors error) {
        return "\"error\": " + error.getResponse();
    }
    
    private final String password;
    private final String login;
    private final BasicAuthenticator basicAuthenticator;
    public final HttpServer webserver;
    
    public WebServer(int port, String user, String password) throws IOException {
        
        this.login = user;
        this.password = password;
        this.basicAuthenticator = new BasicAuthenticator("skcrew") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals(login) && pwd.equals(password);
            }
        };
        
        this.webserver = HttpServer.create(new InetSocketAddress(port), 0);
        webserver.createContext("/", (exchange -> {
            Map<String, List<String>> params;

            if (!exchange.getRequestMethod().toUpperCase().equals("GET") && !exchange.getRequestMethod().toUpperCase().equals("POST")) {
                this.sendResponse(exchange, 405, "{ "+this.build_error(Errors.WRONG_METHOD)+" }");
                return;
            }

            String[] pathSegments = exchange.getRequestURI().getRawPath().split("/");
            int pathSegmentsAmount = pathSegments.length;

            if (pathSegmentsAmount < 2 || pathSegments[1].isEmpty()) {
                this.sendResponse(exchange, 400, "{ "+this.build_error(Errors.WRONG_PATH)+" }");
                return;
            }
            switch(pathSegments[1].toLowerCase()) {
                case "players":
                    if (pathSegmentsAmount < 3 || pathSegments[2].isEmpty()) {
                        params = splitQuery(exchange.getRequestURI().getRawQuery());
                        String players;
                        if (!params.containsKey("server")) {
                            players = String.join(",", Skcrew.getInstance().getSocketServerListener().getPlayers().stream().map(player -> player.toString()).collect(Collectors.toSet()));
                        } else {
                            Set<BaseServer> servers = params.get("server").stream().map(server -> Skcrew.getInstance().getSocketServerListener().getServer(server)).collect(Collectors.toSet());
                            players = String.join(",", Skcrew.getInstance().getSocketServerListener().getPlayers(servers).stream().map(player -> player.toString()).collect(Collectors.toSet()));
                        }
                        this.sendResponse(exchange, 200, "{ \"data\": ["+players+"] }");
                        return;
                    }

                    if (pathSegmentsAmount < 4 || pathSegments[3].isEmpty()) {
                        String player_name = pathSegments[2];
                        NetworkPlayer player;
                        try {
                            player = Skcrew.getInstance().getSocketServerListener().getPlayer(UUID.fromString(player_name)); 
                        } catch (IllegalArgumentException ex) {
                            player = Skcrew.getInstance().getSocketServerListener().getPlayer(player_name);
                        }
                        if (player == null) {
                            this.sendResponse(exchange, 404, "{ "+this.build_error(Errors.PLAYER_NOT_FOUND)+" }");
                            return;
                        }
                        this.sendResponse(exchange, 200, "{ \"data\": "+player.toString(true)+" }");
                        return;
                    }
                    if ((pathSegmentsAmount < 5 || pathSegments[4].isEmpty()) && pathSegments[3].toLowerCase().equals("kick")) {
                        String player_name = pathSegments[2];
                        NetworkPlayer player;
                        try {
                            player = Skcrew.getInstance().getSocketServerListener().getPlayer(UUID.fromString(player_name)); 
                        } catch (IllegalArgumentException ex) {
                            player = Skcrew.getInstance().getSocketServerListener().getPlayer(player_name);
                        }
                        if (player == null) {
                            this.sendResponse(exchange, 404, "{ "+this.build_error(Errors.PLAYER_NOT_FOUND)+" }");
                            return;
                        }
                        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                            player.kick(this.getRequestBody(exchange));
                        } else {
                          player.kick(null);
                        }
                        this.sendResponse(exchange, 200, "{ \"data\": \"Player "+ player.getName() +" kicked\" }");
                        return;
                    }
                    if ((pathSegmentsAmount < 6 || pathSegments[5].isEmpty()) && !pathSegments[4].isEmpty() && pathSegments[3].toLowerCase().equals("connect")) {
                        String player_name = pathSegments[2];
                        String server_name = pathSegments[4];
                        NetworkPlayer player;
                        try {
                            player = Skcrew.getInstance().getSocketServerListener().getPlayer(UUID.fromString(player_name)); 
                        } catch (IllegalArgumentException ex) {
                            player = Skcrew.getInstance().getSocketServerListener().getPlayer(player_name);
                        }
                        if (player == null) {
                            this.sendResponse(exchange, 404, "{ "+this.build_error(Errors.PLAYER_NOT_FOUND)+" }");
                            return;
                        }
                        NetworkServer server = Skcrew.getInstance().getSocketServerListener().getServer(server_name);

                        if (server == null) {
                            this.sendResponse(exchange, 404, "{ "+this.build_error(Errors.SERVER_NOT_FOUND)+" }");
                            return;
                        }
                        if (!server.isConnected()) {
                            this.sendResponse(exchange, 410, "{ "+this.build_error(Errors.SERVER_IS_OFFLINE)+" }");
                            return;
                        }
                        player.connect(server);
                        this.sendResponse(exchange, 200, "{ \"data\": \"Player "+ player.getName() +" connected to server "+server.getName()+"\" }");
                        return;
                    }
                    this.sendResponse(exchange, 400, "{ "+this.build_error(Errors.PLAYER_NOT_SPECIFIED)+" }");
                    return;

                case "servers":
                    if (pathSegmentsAmount < 3 || pathSegments[2].isEmpty()) {
                        params = splitQuery(exchange.getRequestURI().getRawQuery());
                        String servers;
                        if (!params.containsKey("online") || !params.get("online").get(0).toLowerCase().equals("true")) {
                            servers = String.join(",", Skcrew.getInstance().getSocketServerListener().getServers().stream().map(server -> server.toString(true)).collect(Collectors.toSet()));
                        } else {
                            servers = String.join(",", Skcrew.getInstance().getSocketServerListener().getOnlineServers().stream().map(server -> server.toString(true)).collect(Collectors.toSet()));
                        }
                        this.sendResponse(exchange, 200, "{ \"data\": ["+servers+"] }");
                        return;
                    }

                    if (pathSegmentsAmount < 4 || pathSegments[3].isEmpty()) {
                        String server_name = pathSegments[2];
                        NetworkServer server = Skcrew.getInstance().getSocketServerListener().getServer(server_name);
                        if (server == null) {
                            this.sendResponse(exchange, 404, "{ "+this.build_error(Errors.SERVER_NOT_FOUND)+" }");
                            return;
                        }
                        this.sendResponse(exchange, 200, "{ \"data\": "+server.toString(true)+" }");
                        return;
                    }
                    if (pathSegmentsAmount < 5 || pathSegments[4].isEmpty()) {
                        String server_name = pathSegments[2];
                        NetworkServer server = Skcrew.getInstance().getSocketServerListener().getServer(server_name);

                        if (server == null) {
                            this.sendResponse(exchange, 404, "{ "+this.build_error(Errors.SERVER_NOT_FOUND)+" }");
                            return;
                        }
                        if (!server.isConnected()) {
                            this.sendResponse(exchange, 410, "{ "+this.build_error(Errors.SERVER_IS_OFFLINE)+" }");
                            return;
                        }
                        switch (pathSegments[3].toLowerCase()) {
                            case "signal":
                                JsonElement response;
                                try {
                                    response = new Gson().fromJson(this.getRequestBody(exchange), JsonElement.class);
                                } catch (IOException ex) {
                                    this.sendResponse(exchange, 500, "{ "+this.build_error(Errors.CONNECTION_CLOSED)+" }");
                                    return;
                                } catch (JsonSyntaxException ex) {
                                    this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_JSON)+" }");
                                    return;
                                }
                                if (!response.isJsonObject()) {
                                    this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                                    return;
                                }
                                JsonObject body = response.getAsJsonObject();
                                if (!body.has("signals") || !body.get("signals").isJsonArray()) {
                                    this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                                    return;
                                }
                                List<JsonElement> jesignals = new ArrayList();
                                body.getAsJsonArray("signals").forEach(signal -> jesignals.add(signal));
                                for (JsonElement el : jesignals) {
                                    if (el.isJsonObject() && el.getAsJsonObject().has("key") && el.getAsJsonObject().has("data"))
                                        continue;
                                    this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                                    return;
                                }
                                List<JsonObject> josignals = jesignals.stream().map(el -> el.getAsJsonObject()).collect(Collectors.toList());
                                ArrayList<Signal> signals = new ArrayList();
                                for (JsonObject jo : josignals) {
                                    JsonElement key = jo.get("key");
                                    if (!key.isJsonPrimitive() || !key.getAsJsonPrimitive().isString()) {
                                        this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                                        return;
                                    }
                                    JsonElement data = jo.get("data");
                                    if (!data.isJsonArray()) {
                                        this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                                        return;
                                    }
                                    ArrayList<Object> alldata = new ArrayList();
                                    for (JsonElement el : data.getAsJsonArray()) {
                                        if (!el.isJsonPrimitive()) {
                                            this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                                            return;
                                        }
                                        JsonPrimitive prdata = el.getAsJsonPrimitive();
                                        alldata.add(prdata.isString() ? prdata.getAsString() : prdata.getAsNumber());
                                    }
                                    signals.add(new Signal(key.getAsString(),alldata));
                                }
                                PacketOutcomingSignal packet = new PacketOutcomingSignal(signals);
                                server.sendPacket(packet);
                                this.sendResponse(exchange, 200,"{ \"data\": { \"response\": \"Signals successfully sended to servers\" } }" );
                                return;
                            default:
                                break;
                        }
                        this.sendResponse(exchange, 400, "{ "+this.build_error(Errors.WRONG_PATH)+" }");
                        return;
                    }
                    this.sendResponse(exchange, 400, "{ "+this.build_error(Errors.SERVER_NOT_SPECIFIED)+" }");
                    return;
                case "signal":
                    JsonElement response;
                    try {
                        response = new Gson().fromJson(this.getRequestBody(exchange), JsonElement.class);
                    } catch (IOException ex) {
                        this.sendResponse(exchange, 500, "{ "+this.build_error(Errors.CONNECTION_CLOSED)+" }");
                        return;
                    } catch (JsonSyntaxException ex) {
                        this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_JSON)+" }");
                        return;
                    }
                    if (!response.isJsonObject()) {
                        this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                        return;
                    }
                    JsonObject body = response.getAsJsonObject();
                    
                    if (!body.has("servers") || !body.get("servers").isJsonArray()) {
                        this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                        return;
                    }
                    List<JsonElement> servers = new ArrayList();
                    body.getAsJsonArray("servers").forEach(server -> servers.add(server));
                    Set<NetworkServer> receivers = new HashSet();
                    for(JsonElement el : servers) {
                        if (!el.isJsonPrimitive() || !el.getAsJsonPrimitive().isString()) {
                            this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                            return;
                        }
                        NetworkServer receiver = Skcrew.getInstance().getSocketServerListener().getServer(el.getAsString());
                        if (receiver.isConnected()) receivers.add(receiver);
                    }
                    if (!body.has("signals") || !body.get("signals").isJsonArray()) {
                        this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                        return;
                    }
                    List<JsonElement> jesignals = new ArrayList();
                    body.getAsJsonArray("signals").forEach(signal -> jesignals.add(signal));
                    for (JsonElement el : jesignals) {
                        if (el.isJsonObject() && el.getAsJsonObject().has("key") && el.getAsJsonObject().has("data"))
                            continue;
                        this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                        return;
                    }
                    List<JsonObject> josignals = jesignals.stream().map(el -> el.getAsJsonObject()).collect(Collectors.toList());
                    ArrayList<Signal> signals = new ArrayList();
                    for (JsonObject jo : josignals) {
                        JsonElement key = jo.get("key");
                        if (!key.isJsonPrimitive() || !key.getAsJsonPrimitive().isString()) {
                            this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                            return;
                        }
                        JsonElement data = jo.get("data");
                        if (!data.isJsonArray()) {
                            this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                            return;
                        }
                        ArrayList<Object> alldata = new ArrayList();
                        for (JsonElement el : data.getAsJsonArray()) {
                            if (!el.isJsonPrimitive()) {
                                this.sendResponse(exchange, 406, "{ "+this.build_error(Errors.INVALID_SIGNAL_JSON)+" }");
                                return;
                            }
                            JsonPrimitive prdata = el.getAsJsonPrimitive();
                            alldata.add(prdata.isString() ? prdata.getAsString() : prdata.getAsNumber());
                        }
                        signals.add(new Signal(key.getAsString(),alldata));
                    }
                    PacketOutcomingSignal packet = new PacketOutcomingSignal(signals);
                    receivers.forEach(server -> server.sendPacket(packet));
                    this.sendResponse(exchange, 200,"{ \"data\": { \"response\": \"Signals successfully sended to servers\" } }" );
                    return;
                default:
                    this.sendResponse(exchange, 400, "{ "+this.build_error(Errors.WRONG_PATH)+" }");
            }
        })).setAuthenticator(this.basicAuthenticator);
    }
 
    @Override
    public void run() {
       this.webserver.start();
    }
    
    public static Map<String, List<String>> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }
        return Pattern.compile("&").splitAsStream(query)
           .map(s -> Arrays.copyOf(s.split("="), 2))
           .collect(Collectors.groupingBy(s -> decode(s[0]).toLowerCase(), Collectors.mapping(s -> decode(s[1]), Collectors.toList())));
    }
    
    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
    
    public String getAuthToken() {
        return Base64.getEncoder().encodeToString((this.login+":"+this.password).getBytes());
    }
    
    public String getRequestBody(HttpExchange exchange) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = exchange.getRequestBody();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        return stringBuilder.toString();
    }
    
    public void sendResponse(HttpExchange exchange, int code, String body) {
        exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF_8");
        byte[] rawResponseBody = body.getBytes(StandardCharsets.UTF_8);
        try {
            exchange.sendResponseHeaders(code, rawResponseBody.length);
            OutputStream os = exchange.getResponseBody();
            os.write(rawResponseBody);
            os.flush();
            os.close();
        } catch (IOException ex) { }
        exchange.close();
    }

    public void close() {
        this.webserver.stop(5);
    }
} 