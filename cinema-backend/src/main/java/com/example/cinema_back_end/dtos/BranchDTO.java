package com.example.cinema_back_end.dtos;

import java.util.List;

import lombok.Data;

@Data
public class BranchDTO {
    private int id;
    private String imgURL;
    private String name;
    private String diaChi;
    private String phoneNo;
    private List<ScheduleDTO> schedules;
    private List<MovieDTO> movies;
    private Long total;
    private Long totalTicket;
}
