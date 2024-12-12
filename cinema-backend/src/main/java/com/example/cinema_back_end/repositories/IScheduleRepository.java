package com.example.cinema_back_end.repositories;

import com.example.cinema_back_end.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
	List<Schedule> findSchedulesByBranch_Id(Integer branchId);
    List<Schedule> findSchedulesByBranch_IdAndRoom_Id( Integer branchId,Integer movieId);
    List<Schedule> findSchedulesByBranch_IdAndRoom_IdAndMovie_Id(Integer branchId,Integer roomId,Integer movieId);
    @Query("SELECT DISTINCT s.startDate FROM Schedule s")
    List<LocalDate> getAllStartDateSchedule();
}