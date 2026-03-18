package com.ilimitech.delivery.spring.menucategories;

import com.ilimitech.delivery.spring.menucategories.dto.CreateMenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.MenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.UpdateMenuCategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuCategoryServiceImpl implements MenuCategoryService {

    private final MenuCategoryRepository repository;
    private final MenuCategoryMapper mapper;

    public MenuCategoryServiceImpl(MenuCategoryRepository repository, MenuCategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<MenuCategoryDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MenuCategoryDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public MenuCategoryDto create(CreateMenuCategoryDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public MenuCategoryDto update(Long id, UpdateMenuCategoryDto dto) {
        return repository.findById(id).map(existing -> {
            mapper.applyUpdate(existing, dto);
            return mapper.toDto(repository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

