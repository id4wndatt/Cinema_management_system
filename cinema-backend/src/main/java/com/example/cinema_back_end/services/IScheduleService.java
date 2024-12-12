package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.ScheduleDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IScheduleService extends IGeneralService<ScheduleDTO>{
	List<String> getAllStartDateSchedule();

	

	List<ScheduleDTO> getSchedulesByBranchId(Integer branchId);
}
