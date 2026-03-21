package com.ilimitech.delivery.application.port.out;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Address;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {

    public List<Address> findByUserId(Long userId) {
        return list("userId", userId);
    }

    public Address findDefaultByUserId(Long userId) {
        return find("userId = ?1 and isDefault = true", userId).firstResult();
    }
}