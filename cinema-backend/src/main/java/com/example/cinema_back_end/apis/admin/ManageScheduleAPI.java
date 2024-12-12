package com.example.cinema_back_end.apis.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cinema_back_end.dtos.ScheduleDTO;
import com.example.cinema_back_end.dtos.TicketDTO;
import com.example.cinema_back_end.services.IScheduleService;


@RestController
@RequestMapping(value = "/api/admin/schedules", produces = "application/json")
public class ManageScheduleAPI {
    @Autowired
    private IScheduleService scheduleService;
    
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchudules(){
        return new ResponseEntity<>(scheduleService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addSchedule(@RequestBody ScheduleDTO schedule){
    	scheduleService.update(schedule);
        return  new ResponseEntity<String>("Thêm lịch chiếu thành công!" ,HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<String> updateSchedule(@RequestBody ScheduleDTO schedule){
    	scheduleService.update(schedule);
        return  new ResponseEntity<String>("Cập nhật lịch chiếu thành công!" ,HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteSchedule(@Param("scheduleId") Integer scheduleId){
    	scheduleService.remove(scheduleId);
    	return new ResponseEntity<String>("Xóa lịch chiếu thành công!", HttpStatus.OK);
    }
//    @PostMapping
//	public ResponseEntity<List<ScheduleDTO>> getSchedulesByBranch(@Param("branchId") Integer branchId) {
//		return new ResponseEntity<List<ScheduleDTO>>(scheduleService.getSchedulesByBranchId(branchId), HttpStatus.OK);
//	}
}
