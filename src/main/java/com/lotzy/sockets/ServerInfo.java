package com.lotzy.sockets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerInfo implements Serializable {
        private String name;
        private ArrayList<String> players;
        private Boolean online;
        
        public ServerInfo(String name) {
            this.name = name;
            this.players = new ArrayList();
            this.online = false;
        }
        
        public void removePlayer(String nick) {
            players.remove(nick);
        }
        public void addPlayer(String nick) {
            players.add(nick);
        }
        public void setPlayers(String[] players) {
             this.players = new ArrayList<String>(Arrays.asList(players));
        }
        public String[] getPlayers() {
            return players.toArray(new String[0]);
        }
        public String getName() {
            return this.name;
        }
        public void setOnline() {
            this.online = true;
        }
        public void setOffline() {
            this.online = false;
            players.clear();
        }
        public Boolean isOnline() {
            return this.online;
        }
    }