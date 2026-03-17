package com.ilimitech.delivery.spring.dishavailability.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class CreateDishAvailabilityDto {

    @NotNull
    private Long dishId;

    @NotNull
    @Min(0) @Max(6)
    private Integer dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    public CreateDishAvailabilityDto() {}

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}

