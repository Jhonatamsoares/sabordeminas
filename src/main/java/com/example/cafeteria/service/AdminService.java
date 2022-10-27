package com.example.cafeteria.service;

import com.example.cafeteria.model.Comment;
import com.example.cafeteria.model.Dish;
import com.example.cafeteria.model.Admin;
import com.example.cafeteria.model.AdminInfo;
import java.util.List;

public interface AdminService {

    int addDish(String id, Dish dish);

    int removeDish(String id, Dish dish);

    List<Dish> getAllDishes(String id);

    AdminInfo getInformation(String id);

    int updateInfo(String id, AdminInfo info);
}