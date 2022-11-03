package com.example.cafeteria.controller;

import com.example.cafeteria.exception.DishNotExistException;
import com.example.cafeteria.exception.OrderNotFinishedException;
import com.example.cafeteria.exception.PasswordNotMatchException;
import com.example.cafeteria.exception.UserAlreadyExistException;
import com.example.cafeteria.exception.UserNotExistException;
import com.example.cafeteria.model.Comment;
import com.example.cafeteria.model.Dish;
import com.example.cafeteria.model.Order;
import com.example.cafeteria.model.Admin;
import com.example.cafeteria.model.AdminInfo;
import com.example.cafeteria.service.OrderServiceImpl;
import com.example.cafeteria.service.AdminServiceImpl;
import com.example.cafeteria.service.SearchEngineServiceImpl;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminServiceImpl adminService;
    private final OrderServiceImpl orderService;
    private final SearchEngineServiceImpl searchEngineService;

    @Autowired
    public AdminController(AdminServiceImpl adminService,
                                OrderServiceImpl orderService, SearchEngineServiceImpl searchEngineService) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.searchEngineService = searchEngineService;
    }

    @GetMapping(path = "/all")
    public List<Admin> getAllAdmin() {
        return adminService.getUsers();
    }

    @GetMapping(path = "/search/" + "{query}")
    public List<Admin> SearchAdmin(@PathVariable("query") String query) {
        List<Admin> res = new ArrayList<>();
        List<String> ids = searchEngineService.searchAdmin(query);
        if (ids != null) {
            for (String id : ids) {
                if (adminService.getUser(id).isPresent()) {
                    res.add(adminService.getUser(id).get());
                }
            }
        }
        return res;
    }

    @GetMapping(path = "{id}")
    public Admin getAdminById(@PathVariable("id") String id)
            throws UserNotExistException {
        return adminService.getUser(id)
                .orElseThrow(() -> new UserNotExistException("User doesn't exist"));
    }

    @PostMapping(path = "/login")
    public Admin loginAdmin(@RequestBody String jsonUser)
            throws UserNotExistException, PasswordNotMatchException {

        JSONObject user = new JSONObject(jsonUser);
        String userName = user.getString("userName");
        String password = user.getString("password");
        Optional<Admin> admin = adminService.getUserByName(userName);
        if (admin.isEmpty()) {
            throw new UserNotExistException("User doesn't exist");
        }
        if (!adminService.passwordMatch(admin.get().getId(), password)) {
            throw new PasswordNotMatchException("Password doesn't match");
        }
        return admin.get();
    }

    @PostMapping(path = "/register")
    public Admin registerAdmin(@RequestBody String jsonUser)
            throws UserAlreadyExistException {

        JSONObject user = new JSONObject(jsonUser);
        String userName = user.getString("userName");
        String password = user.getString("password");
        String phoneNumber = user.getString("phoneNumber");
        String address = user.getString("address");
        String city = user.getString("city");
        String state = user.getString("state");
        String zip = user.getString("zip");
        Admin admin = adminService
                .addUser(userName, password, phoneNumber, address, city, state, zip);
        if (admin == null) {
            throw new UserAlreadyExistException("User already exists, please login");
        }
        return admin;
    }

    @PostMapping(path = "/logout")
    public int logoutAdmin() {
        System.out.println("logout the user");
        return 1;
    }

    @GetMapping(path = "/myActiveOrders/" + "{id}")
    public List<Order> getActiveOrders(@PathVariable("id") String id)
            throws UserNotExistException {
        if (adminService.getUser(id).isEmpty()) {
            throw new UserNotExistException("The given restaurant doesn't exist");
        }
        return orderService.adminGetActiveOrders(id);
    }

    @GetMapping(path = "/myOrderHistory/" + "{id}")
    public List<Order> getOrderHistory(@PathVariable("id") String id)
            throws UserNotExistException {
        if (adminService.getUser(id).isEmpty()) {
            throw new UserNotExistException("The given restaurant doesn't exist");
        }
        return orderService.adminFindPastOrders(id);
    }

    @GetMapping(path = "/menu/" + "{id}")
    public List<Dish> getMenu(@PathVariable("id") String id)
            throws UserNotExistException {
        if (adminService.getUser(id).isEmpty()) {
            throw new UserNotExistException("The given restaurant doesn't exist");
        }
        return adminService.getAllDishes(id);
    }

    @PostMapping(path = "/addToMenu")
    public int addDishToMenu(@RequestBody String jsonDish)
            throws UserNotExistException {
        JSONObject dish = new JSONObject(jsonDish);
        String adminId = dish.getString("adminId");
        String dishName = dish.getString("dishName");
        String imageUrl = dish.getString("imageUrl");
        double price = dish.getDouble("price");
        Dish newDish = new Dish(dishName, price, imageUrl);
        int res = adminService.addDish(adminId, newDish);
        if (res == -1) {
            throw new UserNotExistException("The given admin doesn't exist");
        }
        // handle search engine
        searchEngineService.addAdmin(dishName, adminId);
        return res;
    }

    @PostMapping(path = "/removeDish")
    public int removeDishFromMenu(@RequestBody String jsonDish)
            throws UserNotExistException, DishNotExistException {
        JSONObject dish = new JSONObject(jsonDish);
        String adminId = dish.getString("adminId");
        Object dishObject = dish.getJSONObject("dish");
        Gson gson = new Gson();
        Dish newDish = gson.fromJson(dishObject.toString(), Dish.class);
        int res = adminService.removeDish(adminId, newDish);
        if (res == -1) {
            throw new UserNotExistException("The given restaurant doesn't exist");
        }
        if (res == 0) {
            throw new DishNotExistException("The given dish doesn't exist");
        }
        // handle search engine
        searchEngineService.removeAdmin(newDish.getDishName(), adminId);
        return res;
    }

    @GetMapping(path = "/information/" + "{id}")
    public AdminInfo getAdminInformation(@PathVariable("id") String id)
            throws UserNotExistException {
        if (adminService.getInformation(id) != null) {
            return adminService.getInformation(id);
        }
        throw new UserNotExistException("The given admin doesn't exist");
    }

    @PostMapping(path = "/information")
    public int updateAdminInformation(@RequestBody String jsonInfo)
            throws UserNotExistException {
        JSONObject object = new JSONObject(jsonInfo);
        String adminId = object.getString("adminId");
        boolean open = object.getBoolean("status");
        String name = object.getString("name");
        String description = object.getString("description");
        String imageUrl = object.getString("imageUrl");
        String tag1 = object.getString("tag1");
        String tag2 = object.getString("tag2");
        String tag3 = object.getString("tag3");
        AdminInfo newInfo = new AdminInfo(open, name, description, imageUrl, tag1, tag2,
                tag3);
        // handle search engine
        AdminInfo oldInfo = adminService.getInformation(adminId);
        if (oldInfo != null) {
            searchEngineService.eraseInfo(oldInfo, adminId);
        }
        searchEngineService.updateInfo(newInfo, adminId);
        int res = adminService.updateInfo(adminId, newInfo);
        if (res == -1) {
            throw new UserNotExistException("The given admin doesn't exist");
        }
        return res;
    }

    @DeleteMapping(path = "{id}")
    public int deleterAdmin(@PathVariable("id") String id)
            throws UserNotExistException, OrderNotFinishedException {
        if (orderService.adminGetActiveOrders(id).size() != 0) {
            throw new OrderNotFinishedException("You still have active orders, please finish them first");
        }
        // handle search engine
        AdminInfo oldInfo = adminService.getInformation(id);
        if (oldInfo != null) {
            searchEngineService.eraseInfo(oldInfo, id);
        }
        List<Dish> dishes = adminService.getAllDishes(id);
        if (dishes != null) {
            searchEngineService.eraseDishes(dishes, id);
        }
        int res = adminService.deleteUser(id);
        if (res == -1) {
            throw new UserNotExistException("User doesn't exist");
        }
        return res;
    }

    @PostMapping(path = "/resetPassword")
    public int resetPassword(@RequestBody String jsonPassword)
            throws UserNotExistException, PasswordNotMatchException {
        JSONObject object = new JSONObject(jsonPassword);
        String id = object.getString("id");
        String oldPassword = object.getString("oldPassword");
        String newPassword = object.getString("newPassword");
        int res = adminService.updatePassword(id, oldPassword, newPassword);
        if (res == -1) {
            throw new UserNotExistException("User doesn't exist");
        }
        if (res == 0) {
            throw new PasswordNotMatchException("Password doesn't match");
        }
        return res;
    }

    @PostMapping(path = "/resetPhone")
    public int resetPhoneNumber(@RequestBody String jsonPhone)
            throws UserNotExistException {
        JSONObject object = new JSONObject(jsonPhone);
        String id = object.getString("id");
        String phoneNumber = object.getString("phoneNumber");
        int res = adminService.updatePhoneNumber(id, phoneNumber);
        if (res == -1) {
            throw new UserNotExistException("User doesn't exist");
        }
        return res;
    }

    @PostMapping(path = "/resetAddress")
    public int resetAddress(@RequestBody String jsonAddress)
            throws UserNotExistException {
        JSONObject object = new JSONObject(jsonAddress);
        String id = object.getString("id");
        String address = object.getString("address");
        String city = object.getString("city");
        String state = object.getString("state");
        String zip = object.getString("zip");
        int res = adminService.updateAddress(id, address, city, state, zip);
        if (res == -1) {
            throw new UserNotExistException("User doesn't exist");
        }
        return res;
    }

    @GetMapping(path = "/getComments/" + "{id}")
    public List<Comment> findCommentsByAdmin(@PathVariable("id") String id)
            throws UserNotExistException {
        Optional<Admin> adminOptional = adminService.getUser(id);
        if (adminOptional.isEmpty()) throw new UserNotExistException("User doesn't exist");
        return orderService.adminGetComments(id);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserNotExistException.class, PasswordNotMatchException.class,
            UserAlreadyExistException.class, DishNotExistException.class,
            OrderNotFinishedException.class})
    public String handleException(Exception e) {
        return e.getMessage();
    }
}