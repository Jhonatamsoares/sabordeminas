package com.example.cafeteria.service;

import com.example.cafeteria.model.Admin;
import com.example.cafeteria.model.Dish;
import com.example.cafeteria.model.AdminInfo;
import java.util.List;

public interface SearchEngineService {
    void addAdmin(String word, String adminId);
    List<String> searchAdmin(String word);
    void removeAdmin(String word, String adminId);
    void eraseInfo(AdminInfo info, String adminId);
    void eraseDishes(List<Dish> dishes, String adminId);
    void updateInfo(AdminInfo info, String adminId);
}