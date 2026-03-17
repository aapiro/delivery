package com.ilimitech.delivery.spring.tips;

import com.ilimitech.delivery.spring.tips.dto.CreateTipDto;
import com.ilimitech.delivery.spring.tips.dto.TipDto;
import com.ilimitech.delivery.spring.tips.dto.UpdateTipDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipServiceImpl implements TipService {

    private final TipRepository repository;
    private final TipMapper mapper;

    public TipServiceImpl(TipRepository repository, TipMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<TipDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TipDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public TipDto create(CreateTipDto dto) {
        Tip saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    public TipDto update(Long id, UpdateTipDto dto) {
        return repository.findById(id).map(existing -> mapper.applyUpdate(existing, dto)).map(repository::save).map(mapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}

