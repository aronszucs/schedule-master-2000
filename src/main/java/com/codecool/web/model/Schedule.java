package com.codecool.web.model;

import java.time.LocalDate;

public class Schedule {

    private int id;
    private int userId;
    private String name;
    private LocalDate startingDate;
    private int durationInDays;

    public Schedule(int id, int userId, String name, LocalDate startingDate, int durationInDays) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.startingDate = startingDate;
        this.durationInDays = durationInDays;
    }
}
