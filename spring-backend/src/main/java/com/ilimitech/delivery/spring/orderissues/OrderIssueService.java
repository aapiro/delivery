package com.ilimitech.delivery.spring.orderissues;
import com.ilimitech.delivery.spring.orderissues.dto.CreateOrderIssueDto;
import com.ilimitech.delivery.spring.orderissues.dto.OrderIssueDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderIssueService {
    private final OrderIssueRepository repo;
    private final OrderIssueMapper mapper;
    public OrderIssueService(OrderIssueRepository repo, OrderIssueMapper mapper) { this.repo = repo; this.mapper = mapper; }
    public List<OrderIssueDto> findAll() { return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList()); }
    public OrderIssueDto create(CreateOrderIssueDto dto) { return mapper.toDto(repo.save(mapper.toEntity(dto))); }
    public void delete(Long id) { repo.deleteById(id); }
}

