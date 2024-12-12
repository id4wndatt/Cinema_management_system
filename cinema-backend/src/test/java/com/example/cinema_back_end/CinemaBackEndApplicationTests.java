package com.example.cinema_back_end;

import com.example.cinema_back_end.repositories.IScheduleRepository;
import com.example.cinema_back_end.services.IBranchService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class CinemaBackEndApplicationTests {
    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private IBranchService branchService;
    @Test
    void testBranch() {
    	branchService.getBranchesAndSchedules();
    }

}
