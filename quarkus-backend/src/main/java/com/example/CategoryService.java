package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.listAll();
    }

    public List<Category> getAllCategoriesFiltered(String name, Boolean isActive) {
        // Get all categories first
        List<Category> categories = categoryRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            categories = categories.stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (isActive != null) {
            categories = categories.stream()
                .filter(c -> c.isActive() == isActive)
                .collect(Collectors.toList());
        }
        
        return categories;
    }

    @Transactional
    public Category addCategory(Category category) {
        categoryRepository.persist(category);
        return category;
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id);

        if (existingCategory == null) {
            throw new NotFoundException("Category not found");
        }

        // Update fields from the updatedCategory
        existingCategory.setName(updatedCategory.getName());
        
        return existingCategory;
    }

    @Transactional
    public Category toggleCategoryStatus(Long id) {
        Category category = categoryRepository.findById(id);
        
        if (category == null) {
            throw new NotFoundException("Category not found");
        }
        
        // Toggle the active status
        Boolean currentStatus = category.isActive();
        if (currentStatus == null) {
            currentStatus = false;
        }
        category.setActive(!currentStatus);
        
        return category;
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id);

        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        categoryRepository.delete(category);
    }
    
    /**
     * Search categories by name
     */
    public List<Category> searchCategories(String name) {
        List<Category> categories = categoryRepository.listAll();
        
        // Apply filter if provided
        if (name != null && !name.isEmpty()) {
            categories = categories.stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return categories;
    }
}