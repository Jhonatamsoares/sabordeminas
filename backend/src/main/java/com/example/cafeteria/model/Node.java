package com.example.cafeteria.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
@Document
public class Node {

    // childMap stores its descendent
    Map<Character, Node> childMap = new HashMap<>();
    // infoMap store adminId and count
    Map<String, Integer> infoMap = new HashMap<>();

    public Node() {
    }

    public void insertAdmin(String adminId) {
        if (!this.infoMap.containsKey(adminId)) {
            this.infoMap.put(adminId, 1);
        } else {
            int count = this.infoMap.get(adminId);
            this.infoMap.put(adminId, count + 1);
        }
    }

    public void deleteAdmin(String adminId) {
        if (this.infoMap.containsKey(adminId)) {
            int count = this.infoMap.get(adminId);
            if (count == 1) {
                this.infoMap.remove(adminId);
            } else {
                this.infoMap.put(adminId, count - 1);
            }
        }
    }
}