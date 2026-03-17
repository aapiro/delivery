package com.ilimitech.delivery.spring.dishavailability.dto;

import java.time.LocalTime;

public class DishAvailabilityDto {
    private Long id;
    private Long dishId;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public DishAvailabilityDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}

