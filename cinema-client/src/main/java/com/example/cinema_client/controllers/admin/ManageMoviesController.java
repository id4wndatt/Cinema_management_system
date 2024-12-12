package com.example.cinema_client.controllers.admin;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.MovieDTO;
import com.example.cinema_client.models.JwtResponseDTO;

@Controller
@RequestMapping("/admin/movies")
public class ManageMoviesController {
	@Autowired
	private RestTemplate restTemplate;
	private static final String API_GET_MOVIES = Api.baseURL + "/api/admin/movies";

	@GetMapping
	public String displayManageMoviePage(HttpSession session, Model model) {
		// Gắn access token jwt vào header để gửi kèm request
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		JwtResponseDTO jwtResponseDTO = (JwtResponseDTO) session.getAttribute("jwtResponse");
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwtResponseDTO.getAccessToken());
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<MovieDTO[]> response = restTemplate.exchange(API_GET_MOVIES, HttpMethod.GET, entity, MovieDTO[].class);
		MovieDTO[] movies = response.getBody();
		model.addAttribute("movies", movies);
		return "admin/manage-movie";
	}
	@GetMapping("/update")
    public String displayUpdateSchedulePage(@ModelAttribute("modelMovie") MovieDTO movie ,HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
	    
       
        
	    model.addAttribute("movie",movie);
	  
        return "admin/update-movie";
    }
	
	@PostMapping("/update")
    public String updateMoviePage(@ModelAttribute("movie") MovieDTO movie,
    		@RequestParam(name = "checkShowing", required = false, defaultValue = "false") boolean checkShowing,
    		HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        //kiểm tra phim đang chiếu hay sắp chiếu
    	if(checkShowing) {
    		movie.setIsShowing(1);
    	}
    	else {movie.setIsShowing(0);
    	}
        HttpEntity<?> entity = new HttpEntity<>(movie,headers);
        try {
        	ResponseEntity<String> response = restTemplate.exchange(API_GET_MOVIES,HttpMethod.PUT, entity, String.class);
        } catch (Exception e) {
			System.out.println(e);
		}
        return "redirect:/admin/movies";
    }
	@GetMapping("/add")
    public String displayAddMoviePage(HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request      
	 
	    model.addAttribute("movie",new MovieDTO());
        return "admin/update-movie";
    }
    @PostMapping("/add")
    public String addMoviePage(@ModelAttribute("movie") MovieDTO movie ,
    		@RequestParam(name = "checkShowing", required = false, defaultValue = "false") boolean checkShowing,
    		HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        //kiểm tra phim đang chiếu hay sắp chiếu
    	if(checkShowing) {
    		movie.setIsShowing(1);
    	}
    	else {movie.setIsShowing(0);
    	}
    	
        HttpEntity<MovieDTO> entity = new HttpEntity<>(movie,headers);
        try {
        	ResponseEntity<String> response = restTemplate.exchange(API_GET_MOVIES,HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			System.out.println(e);
		}
	    
        return "redirect:/admin/movies";
    }
//	
}
