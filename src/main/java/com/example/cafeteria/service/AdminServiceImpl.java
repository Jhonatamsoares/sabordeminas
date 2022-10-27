package com.example.cafeteria.service;

import com.example.cafeteria.model.Dish;
import com.example.cafeteria.model.Admin;
import com.example.cafeteria.model.AdminInfo;
import com.example.cafeteria.repository.AdminRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService, UserService<Admin> {

    @Autowired
    AdminRepository adminRepository;
    PasswordService passwordService = new PasswordService();


    @Override
    public int addDish(String id, Dish dish) {
        Optional<Admin> admin = this.getUser(id);
        if (admin.isPresent()) {
            Set<Dish> set;
            if (admin.get().getMenu() == null) {
                set = new HashSet<>();
            } else {
                set = new HashSet<>(admin.get().getMenu());
            }
            set.add(dish);
            admin.get().setMenu(new ArrayList<>(set));
            adminRepository.save(admin.get());

            System.out.println("Add the dish");
            return 1;
        }
        System.out.println("Can't add the dish");
        return -1;
    }

    @Override
    public int removeDish(String id, Dish dish) {
        Optional<Admin> admin = this.getUser(id);
        if (admin.isPresent()) {
            List<Dish> temp = admin.get().getMenu();
            if (temp.contains(dish)) {
                temp.remove(dish);
                admin.get().setMenu(temp);
                adminRepository.save(admin.get());

                System.out.println("Remove the dish");
                return 1;
            } else {
                System.out.println("Dish not in the menu");
                return 0;
            }
        }
        System.out.println("Can't remove the dish");
        return -1;
    }

    @Override
    public List<Dish> getAllDishes(String id) {
        Optional<Admin> admin = this.getUser(id);
        System.out.println("Get all dishes from admin: " + id);
        return admin.map(Admin::getMenu).orElse(null);
    }

    @Override
    public AdminInfo getInformation(String id) {
        Optional<Admin> admin = this.getUser(id);
        if (admin.isPresent()) {
            System.out.println("Get the admin information");
            if (admin.get().getInformation() == null) {
                return new AdminInfo();
            } else {
                return admin.get().getInformation();
            }
        }
        return null;
    }

    @Override
    public int updateInfo(String id, AdminInfo info) {
        Optional<Admin> admin = this.getUser(id);
        if (admin.isPresent()) {

            admin.get().setInformation(info);
            adminRepository.save(admin.get());
            System.out.println("Update the information");
            return 1;
        }
        System.out.println("Can't update the information");
        return -1;
    }

    @Override
    public Admin addUser(String userName, String password, String phoneNumber, String address,
                              String city, String state, String zip) {
        if (this.getUserIdByName(userName) == null) {
            String newPassword = passwordService.generatePassword(password);
            Admin admin = new Admin(userName, newPassword, phoneNumber, address, city,
                    state,
                    zip);
            adminRepository.insert(admin);
            System.out.println("Admin added to the database");
            return admin;
        }
        System.out.println("Admin can't be added to the database");
        return null;
    }

    @Override
    public int deleteUser(String id) {
        if (this.getUser(id).isPresent()) {
            adminRepository.deleteById(id);
            System.out.println("Admin deleted from the database");
            return 1;
        }
        System.out.println("Admin can't be deleted from the database");
        return -1;
    }

    @Override
    public Optional<Admin> getUser(String id) {
        if (id != null) {
            return adminRepository.findById(id);
        }
        return Optional.empty();
    }

    @Override
    public String getUserIdByName(String userName) {
        List<Admin> admins = this.getUsers();
        for (Admin admin : admins) {
            if (admin.getUserName().equals(userName)) {
                return admin.getId();
            }
        }
        System.out.println("Given userName doesn't found in admin database");
        return null;
    }

    @Override
    public Optional<Admin> getUserByName(String userName) {
        return this.getUser(getUserIdByName(userName));
    }

    @Override
    public List<Admin> getUsers() {
        return adminRepository.findAll();
    }

    @Override
    public boolean passwordMatch(String id, String password) {
        Optional<Admin> admin = this.getUser(id);
        return admin.isPresent() && passwordService
                .passwordMatch(password, admin.get().getPassword());
    }

    @Override
    public int updatePassword(String id, String oldPassword, String newPassword) {
        Optional<Admin> admin = this.getUser(id);
        if (admin.isPresent()) {
            if (this.passwordMatch(id, oldPassword)) {
                admin.get().setPassword(passwordService.generatePassword(newPassword));
                adminRepository.save(admin.get());
                System.out.println("Update the password");
                return 1;
            } else {
                System.out.println("Password doesn't match");
                return 0;
            }
        }
        System.out.println("Can't update the password");
        return -1;
    }

    @Override
    public int updatePhoneNumber(String id, String newNumber) {
        Optional<Admin> admin = this.getUser(id);
        if (admin.isPresent()) {
            admin.get().setPhoneNumber(newNumber);
            adminRepository.save(admin.get());
            System.out.println("Update the phone number");
            return 1;
        }
        System.out.println("Can't update the phone number");
        return -1;
    }

    @Override
    public int updateAddress(String id, String address, String city, String state,
                             String zip) {
        Optional<Admin> admin = this.getUser(id);
        if (admin.isPresent()) {
            admin.get().setAddress(address);
            admin.get().setCity(city);
            admin.get().setState(state);
            admin.get().setZip(zip);
            adminRepository.save(admin.get());
            System.out.println("Update the address");
            return 1;
        }
        System.out.println("Can't update the address");
        return -1;
    }
}