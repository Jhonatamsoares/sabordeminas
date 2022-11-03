package com.example.cafeteria.service;

import com.example.cafeteria.model.Dish;
import com.example.cafeteria.model.AdminInfo;
import com.example.cafeteria.model.SearchEngine;
import com.example.cafeteria.repository.SearchEngineRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchEngineServiceImpl implements SearchEngineService {

    @Autowired
    SearchEngineRepository searchEngineRepository;

    @Override
    public void addAdmin(String word, String adminId) {
        Optional<SearchEngine> optionalSearchEngine = searchEngineRepository.findById("1");
        if (optionalSearchEngine.isEmpty()) return;
        SearchEngine searchEngine = optionalSearchEngine.get();
        searchEngine.add(word, adminId);
        searchEngineRepository.save(searchEngine);
    }

    @Override
    public List<String> searchAdmin(String word) {
        Optional<SearchEngine> optionalSearchEngine = searchEngineRepository.findById("1");
        if (optionalSearchEngine.isEmpty()) return null;
        SearchEngine searchEngine = optionalSearchEngine.get();
        searchEngineRepository.save(searchEngine);
        return searchEngine.search(word);
    }

    @Override
    public void removeAdmin(String word, String adminId) {
        Optional<SearchEngine> optionalSearchEngine = searchEngineRepository.findById("1");
        if (optionalSearchEngine.isEmpty()) return;
        SearchEngine searchEngine = optionalSearchEngine.get();
        searchEngine.remove(word, adminId);
        searchEngineRepository.save(searchEngine);
    }

    @Override
    public void eraseInfo(AdminInfo info, String adminId) {
        this.removeAdmin(info.getAdminName(), adminId);
        this.removeAdmin(info.getTag1(), adminId);
        this.removeAdmin(info.getTag2(), adminId);
        this.removeAdmin(info.getTag3(), adminId);
    }

    @Override
    public void eraseDishes(List<Dish> dishes, String adminId) {
        for (Dish dish : dishes) {
            this.removeAdmin(dish.getDishName(), adminId);
        }
    }

    @Override
    public void updateInfo(AdminInfo info, String adminId) {
        this.addAdmin(info.getAdminName(), adminId);
        this.addAdmin(info.getTag1(), adminId);
        this.addAdmin(info.getTag2(), adminId);
        this.addAdmin(info.getTag3(), adminId);
    }
}