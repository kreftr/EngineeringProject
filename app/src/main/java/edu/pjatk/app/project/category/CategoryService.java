package edu.pjatk.app.project.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public void removeCategory(Category category){
        categoryRepository.remove(category);
    }

    public List<String> getAllCategories(){
        List<String> listOfCategories = new ArrayList<>();
        Optional<List<Category>> categories = categoryRepository.getAll();
        if (categories.isPresent() && !categories.get().isEmpty()){
            for (Category c : categories.get()){
                listOfCategories.add(c.getTitle());
            }
            Collections.sort(listOfCategories);
            return listOfCategories;
        }
        else return Collections.emptyList();
    }

    public Optional<Category> getCategoryByTitle(String title){
        return categoryRepository.getByTitle(title);
    }

}
