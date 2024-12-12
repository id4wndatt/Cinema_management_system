package com.example.cinema_back_end.apis;

import com.example.cinema_back_end.dtos.ScheduleDTO;
import com.example.cinema_back_end.services.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/schedule", produces = "application/json")
public class ScheduleApi {
    @Autowired
    private IScheduleService scheduleService;


    @GetMapping
    public ScheduleDTO getSchedule(@RequestParam Integer scheduleId){
        return scheduleService.getById(scheduleId);
    }
    @GetMapping("/all-schedule-dates")
    public List<String> getAllStartDate(){
        return  scheduleService.getAllStartDateSchedule();
    }
}
