package com.example.cinema_client.models;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDTO {
    private int id;
    private String name;
    private String smallImageURl;
    private String shortDescription;
    private String longDescription;
    private String largeImageURL;
    private String director;
    private String actors;
    private String categories;
    private LocalDate releaseDate;
    private int duration;
    private String trailerURL;
    private String language;
    private String rated;
    private Integer isShowing;
    private List<ScheduleDTO> schedules;
    private Long total;
    private Long totalTicket;
}
