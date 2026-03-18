package com.ilimitech.delivery.spring.menucategories;

import com.ilimitech.delivery.spring.menucategories.dto.CreateMenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.MenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.UpdateMenuCategoryDto;

import java.util.List;

public interface MenuCategoryService {
    List<MenuCategoryDto> findAll();
    MenuCategoryDto findById(Long id);
    MenuCategoryDto create(CreateMenuCategoryDto dto);
    MenuCategoryDto update(Long id, UpdateMenuCategoryDto dto);
    void delete(Long id);
}

