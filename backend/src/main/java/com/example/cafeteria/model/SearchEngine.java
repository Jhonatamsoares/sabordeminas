package com.example.cafeteria.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SearchEngine {

    @Id
    String id = "1";
    Node root;

    public SearchEngine() {
        this.root = new Node();
    }

    public void add(String word, String adminId) {
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = Character.toLowerCase(word.charAt(i));
            if(c == ' ') continue;
            if (!cur.childMap.containsKey(c)) {
                cur.childMap.put(c, new Node());
            }
            cur.insertAdmin(adminId);
            cur = cur.childMap.get(c);
        }
        cur.insertAdmin(adminId);
    }

    public List<String> search(String word) {
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = Character.toLowerCase(word.charAt(i));
            if(c == ' ') continue;
            if (!cur.childMap.containsKey(c)) {
                return null;
            }
            cur = cur.childMap.get(c);
        }
        return new ArrayList<>(cur.infoMap.keySet());
    }

    public void remove(String word, String adminId) {
        Node cur = root;
        if (word == null) return;
        for (int i = 0; i < word.length(); i++) {
            char c = Character.toLowerCase(word.charAt(i));
            if(c == ' ') continue;
            if (!cur.childMap.containsKey(c)) return;
            cur.deleteAdmin(adminId);
            cur = cur.childMap.get(c);
        }
        cur.deleteAdmin(adminId);
    }
}