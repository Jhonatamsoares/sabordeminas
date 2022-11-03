package com.example.cafeteria.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AdminInfo {
    @Id
    private boolean open;

    private String adminName;
    private String description;
    private String imageUrl;
    private String tag1;
    private String tag2;
    private String tag3;

    public AdminInfo() {
    }

    public AdminInfo(boolean open, String adminName, String description,
                          String imageUrl, String tag1, String tag2, String tag3) {
        this.open = open;
        this.adminName = adminName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    @Override
    public String toString() {
        return "AdminInfo{" +
                "open=" + open +
                ", adminName='" + adminName + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", tag3='" + tag3 + '\'' +
                '}';
    }
}