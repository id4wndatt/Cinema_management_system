package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.BranchDTO;
import com.example.cinema_back_end.dtos.MovieDTO;
import com.example.cinema_back_end.dtos.ScheduleDTO;
import com.example.cinema_back_end.entities.Branch;
import com.example.cinema_back_end.entities.Schedule;
import com.example.cinema_back_end.repositories.IBranchRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BranchService implements IBranchService{

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<BranchDTO> getBranchesThatShowTheMovie(Integer movieId) {
		LocalDate date=LocalDate.now();
		LocalTime time=LocalTime.now();
		List<BranchDTO> branchDTOs=new ArrayList<BranchDTO>();
		for(Branch branch: branchRepository.getBranchThatShowTheMovie(movieId)) {
			BranchDTO branchDTO=modelMapper.map(branch, BranchDTO.class);
			branchDTO.setSchedules(new LinkedList<ScheduleDTO>());
			for(Schedule schedule: branch.getScheduleList()) {
	               if(schedule.getStartDate().compareTo(date)>0 ||
	    				(schedule.getStartDate().compareTo(date)==0 && schedule.getStartTime().compareTo(time)>0)	) {
	                		branchDTO.getSchedules().add(modelMapper.map(schedule,ScheduleDTO.class));
	                }
	        }
			if(branchDTO.getSchedules().size()>0) {
				branchDTOs.add(branchDTO);
			}
		}
		return branchDTOs;
    }

	@Override
	public BranchDTO getBranchAndSchedules(Integer branchId) {
		Branch branch= branchRepository.findByIdAndFetchSchedulesEagerly(branchId);
		BranchDTO branchDTO=modelMapper.map(branch, BranchDTO.class);
		branchDTO.setSchedules(new LinkedList<ScheduleDTO>());
		LocalDate date=LocalDate.now();
		LocalTime time=LocalTime.now();
		for(Schedule schedule: branch.getScheduleList()) {
               if(schedule.getStartDate().compareTo(date)>0 ||
    				(schedule.getStartDate().compareTo(date)==0 && schedule.getStartTime().compareTo(time)>0)	) {
                		branchDTO.getSchedules().add(modelMapper.map(schedule,ScheduleDTO.class));
                }
        }
        return branchDTO;
	}

	@Override
	public List<BranchDTO> getBranchesAndSchedules() {
		LocalDate date=LocalDate.now();
		LocalTime time=LocalTime.now();
		return branchRepository.findAll().stream().map(branch -> {
				BranchDTO branchDTO=modelMapper.map(branch, BranchDTO.class);
				Map<Integer,MovieDTO> movieByBranch= new HashMap<>();
	        	List<Schedule> schedules=branch.getScheduleList();
	        	for (Schedule schedule : schedules) {
					if(schedule.getStartDate().compareTo(date)>0 ||
						(schedule.getStartDate().compareTo(date)==0 && schedule.getStartTime().compareTo(time)>0)	) {
		        		if(movieByBranch.get(schedule.getMovie().getId())==null) {	
		        			ScheduleDTO scheduleDTO=modelMapper.map(schedule,ScheduleDTO.class);
		        			scheduleDTO.getMovie().setSchedules(new ArrayList<>());
		        			scheduleDTO.getMovie().getSchedules().add(modelMapper.map(schedule,ScheduleDTO.class));
		        			movieByBranch.put(schedule.getMovie().getId(), scheduleDTO.getMovie());
		        			
		        		}else {
		        			movieByBranch.get(schedule.getMovie().getId()).getSchedules()
		        			.add(modelMapper.map(schedule,ScheduleDTO.class));
		        		}
					}
				}
	        	branchDTO.setMovies(new ArrayList<MovieDTO>(movieByBranch.values()));
	        	return branchDTO;
			})
			.collect(Collectors.toList());
	}

	@Override
	public List<BranchDTO> findAll() {
		return branchRepository.findAll().stream().map(branch -> modelMapper.map(branch, BranchDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public BranchDTO getById(Integer id) {
		return modelMapper.map(branchRepository.getById(id),BranchDTO.class);
	}

	@Override
	public void update(BranchDTO branch) {
		
		branchRepository.save(modelMapper.map(branch, Branch.class));
		
	}

	@Override
	public void remove(Integer id) {
		branchRepository.deleteById(id);
		
	}
}
