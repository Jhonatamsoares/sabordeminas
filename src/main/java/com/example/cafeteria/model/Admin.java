package com.example.cafeteria.model;

import java.util.List;

public class Admin extends User {
    private AdminInfo information;
    private List<Dish> menu;

    public Admin() {
        this.setType("admin");
    }

    public Admin(String userName, String password, String phoneNumber, String address,
                      String city, String state, String zip, AdminInfo information,
                      List<Dish> menu) {
        super(userName, password, phoneNumber, address, city, state, zip);
        this.setType("restaurant");
        this.information = information;
        this.menu = menu;
    }

    public Admin(String userName, String password, String phoneNumber, String address,
                      String city, String state, String zip) {
        super(userName, password, phoneNumber, address, city, state, zip);
        this.setType("admin");
    }

    public AdminInfo getInformation() {
        return information;
    }

    public void setInformation(AdminInfo information) {
        this.information = information;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "information=" + information +
                ", menu=" + menu +
                '}';
    }
}