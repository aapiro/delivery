package com.ilimitech.delivery.service;

import com.ilimitech.delivery.domain.Category;
import com.ilimitech.delivery.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getAllCategoriesFiltered(String name, Boolean isActive) {
        List<Category> categories = categoryRepository.findAll();

        if (name != null && !name.isEmpty()) {
            categories = categoryRepository.findByNameContainingIgnoreCase(name);
        }

        if (isActive != null) {
            categories.removeIf(c -> c.getActive() == null || !c.getActive().equals(isActive));
        }

        return categories;
    }

    @Transactional
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existing = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        existing.setName(updatedCategory.getName());
        return categoryRepository.save(existing);
    }

    @Transactional
    public Category toggleCategoryStatus(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        Boolean current = category.getActive();
        if (current == null) current = false;
        category.setActive(!current);
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> searchCategories(String name) {
        if (name == null || name.isEmpty()) return categoryRepository.findAll();
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}

