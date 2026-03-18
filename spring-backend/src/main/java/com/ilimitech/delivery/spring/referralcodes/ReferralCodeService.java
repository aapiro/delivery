package com.ilimitech.delivery.spring.referralcodes;
import com.ilimitech.delivery.spring.referralcodes.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ReferralCodeService {
    private final ReferralCodeRepository repo; private final ReferralCodeMapper mapper;
    public ReferralCodeService(ReferralCodeRepository repo, ReferralCodeMapper mapper) { this.repo = repo; this.mapper = mapper; }
    public List<ReferralCodeDto> findAll() { return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList()); }
    public ReferralCodeDto findById(Long id) { return repo.findById(id).map(mapper::toDto).orElse(null); }
    public ReferralCodeDto create(CreateReferralCodeDto dto) { return mapper.toDto(repo.save(mapper.toEntity(dto))); }
    public ReferralCodeDto update(Long id, UpdateReferralCodeDto dto) {
        return repo.findById(id).map(e -> mapper.applyUpdate(e, dto)).map(repo::save).map(mapper::toDto).orElse(null);
    }
    public void delete(Long id) { repo.findById(id).ifPresent(repo::delete); }
}
