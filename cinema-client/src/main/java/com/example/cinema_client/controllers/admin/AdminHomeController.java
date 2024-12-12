package com.example.cinema_client.controllers.admin;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.BranchDTO;
import com.example.cinema_client.models.JwtResponseDTO;
import com.example.cinema_client.models.MovieDTO;
import com.example.cinema_client.models.TicketDTO;



@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    private RestTemplate restTemplate;
   // private static final String API_GET_TICKETS = Api.baseURL+"/api/admin/tickets";
	private static final String API_GET_BRANCHES = Api.baseURL + "/api/admin/report/branches";
	private static final String API_GET_MOVIES = Api.baseURL + "/api/admin/report/movies";
	private static final String API_SCHEDULES = Api.baseURL + "/api/admin/schedules";
	@GetMapping
    public String displayHomePage(HttpSession session,Model model){
        // Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<MovieDTO[]> responses = restTemplate.exchange(API_GET_MOVIES, HttpMethod.GET, entity, MovieDTO[].class);
		MovieDTO[] movies = responses.getBody();
		ResponseEntity<BranchDTO[]> response = restTemplate.exchange(API_GET_BRANCHES, HttpMethod.GET, entity, BranchDTO[].class);
		BranchDTO[] branches = response.getBody();
		
		Long turnover = 0l;
		Long numTicket = 0l;
		for(MovieDTO movie: movies) {
			turnover += movie.getTotal();
			numTicket += movie.getTotalTicket();
		}
		model.addAttribute("turnover", turnover);
		model.addAttribute("numTicket", numTicket);
		model.addAttribute("movies", movies);
		model.addAttribute("branches", branches);
        return "admin/home-admin";
    }
}
