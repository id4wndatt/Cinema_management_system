package com.example.cinema_back_end.apis.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cinema_back_end.dtos.BranchDTO;
import com.example.cinema_back_end.dtos.MovieDTO;
import com.example.cinema_back_end.dtos.ScheduleDTO;
import com.example.cinema_back_end.dtos.TicketDTO;
import com.example.cinema_back_end.services.IBranchService;
import com.example.cinema_back_end.services.IMovieService;
import com.example.cinema_back_end.services.ITicketService;

@RestController
@RequestMapping("/api/admin/report")
public class ManageReportAPI {
	@Autowired
	private ITicketService ticketService;

	@Autowired
	private IBranchService branchService;
	
	@Autowired
	private IMovieService movieService;

	@GetMapping("/branches")
	public ResponseEntity<List<BranchDTO>> getAllBranches() {
		List<TicketDTO> tickets = ticketService.findAll();

		Map<Integer, BranchDTO> branches = new HashMap<>();
		ScheduleDTO schedule;
		BranchDTO branch;
		Long vipPrice;
		for (BranchDTO b : branchService.findAll()) {
			b.setTotal(0l);
			b.setTotalTicket(0l);
			branches.put(b.getId(), b);
		}
		for (TicketDTO ticket : tickets) {
			schedule = ticket.getSchedule();
			
			vipPrice = ticket.getSeat().isVip() ? 10000L : 0L;
			branch = branches.get(schedule.getBranch().getId());
			branch.setTotal(ticket.getSchedule().getPrice().longValue() + branch.getTotal() + vipPrice);
			branch.setTotalTicket(branch.getTotalTicket() + 1);

		}

		return new ResponseEntity<>(new ArrayList(branches.values()), HttpStatus.OK);

	}
	@GetMapping("/movies")
	public ResponseEntity<List<MovieDTO>> getAllMovies() {
		List<TicketDTO> tickets = ticketService.findAll();

		Map<Integer, MovieDTO> movies = new HashMap<>();
		ScheduleDTO schedule;
		MovieDTO movie;
		Long vipPrice;
		for (MovieDTO b : movieService.findAll()) {
			b.setTotal(0l);
			b.setTotalTicket(0l);
			movies.put(b.getId(), b);
		}
		for (TicketDTO ticket : tickets) {
			schedule = ticket.getSchedule();
			
			vipPrice = ticket.getSeat().isVip() ? 10000L : 0L;
			movie = movies.get(schedule.getMovie().getId());
			movie.setTotal(ticket.getSchedule().getPrice().longValue() + movie.getTotal() + vipPrice);
			movie.setTotalTicket(movie.getTotalTicket() + 1);
			
		}

		return new ResponseEntity<>(new ArrayList(movies.values()), HttpStatus.OK);

	}

}
