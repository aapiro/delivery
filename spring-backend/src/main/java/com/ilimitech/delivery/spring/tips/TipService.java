package com.ilimitech.delivery.spring.tips;

import com.ilimitech.delivery.spring.tips.dto.CreateTipDto;
import com.ilimitech.delivery.spring.tips.dto.TipDto;
import com.ilimitech.delivery.spring.tips.dto.UpdateTipDto;

import java.util.List;

public interface TipService {
    List<TipDto> findAll();
    TipDto findById(Long id);
    TipDto create(CreateTipDto dto);
    TipDto update(Long id, UpdateTipDto dto);
    void delete(Long id);
}

