package com.ilimitech.delivery.infrastructure.mapper;

import com.ilimitech.delivery.infrastructure.adapter.in.rest.generated.model.Restaurant;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.generated.model.RestaurantSummary;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.CuisineType;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta")
public interface RestaurantMapper {

    @Mapping(target = "cuisine", source = "cuisines", qualifiedByName = "mapCuisinesToString")
    @Mapping(target = "imageUrl", source = "imageUrl", qualifiedByName = "stringToUri")
    RestaurantSummary toSummary(RestaurantEntity entity);

    @Mapping(target = "cuisine", source = "cuisines", qualifiedByName = "mapCuisinesToString")
    @Mapping(target = "imageUrl", source = "imageUrl", qualifiedByName = "stringToUri")
    @Mapping(target = "dishes", ignore = true)
    Restaurant toRestaurant(RestaurantEntity entity);




    // Mapeo para la lista de la paginación
    List<RestaurantSummary> toSummaryList(List<RestaurantEntity> entities);

    // LÓGICA PERSONALIZADA

    @Named("mapCuisinesToString")
    default String mapCuisinesToString(Set<CuisineType> cuisines) {
        if (cuisines == null || cuisines.isEmpty()) {
            return "General";
        }
        // Unimos los nombres de las cocinas por coma (ej: "Italiana, Pizza")
        return cuisines.stream()
                .map(CuisineType::getName) // Asumiendo que CuisineType tiene getName()
                .collect(Collectors.joining(", "));
    }

    @Named("stringToUri")
    default URI stringToUri(String url) {
        if (url == null || url.isEmpty()) return null;
        try {
            return URI.create(url);
        } catch (Exception e) {
            return null;
        }
    }
}
