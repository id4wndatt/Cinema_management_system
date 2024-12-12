package com.example.cinema_client.controllers.admin;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.BranchDTO;
import com.example.cinema_client.models.JwtResponseDTO;
import com.example.cinema_client.models.MovieDTO;
import com.example.cinema_client.models.RoomDTO;
import com.example.cinema_client.models.ScheduleDTO;
import com.example.cinema_client.models.User;

@Controller
@RequestMapping("/admin/branches")
public class ManageBranchesController {

	@Autowired
	private RestTemplate restTemplate;
	private static final String API_GET_BRANCHES = Api.baseURL + "/api/admin/branches";
	
	@GetMapping
	public String displayManageBranchPage(HttpSession session, Model model) {
		// Gắn access token jwt vào header để gửi kèm request
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		JwtResponseDTO jwtResponseDTO = (JwtResponseDTO) session.getAttribute("jwtResponse");
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwtResponseDTO.getAccessToken());
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<BranchDTO[]> response = restTemplate.exchange(API_GET_BRANCHES, HttpMethod.GET, entity, BranchDTO[].class);
		BranchDTO[] branches = response.getBody();
		model.addAttribute("branches", branches);
		return "admin/manage-branch";
	}
	@GetMapping("/update")
    public String displayUpdateBranchPage(@ModelAttribute("modelBranch") BranchDTO branch ,HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
	    
       
        
	    model.addAttribute("branch",branch);
	  
        return "admin/update-branch";
    }
	
	@PostMapping("/update")
    public String updateBranchPage(@ModelAttribute("branch") BranchDTO branch,HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(branch,headers);
        try {
        	ResponseEntity<String> response = restTemplate.exchange(API_GET_BRANCHES,HttpMethod.PUT, entity, String.class);
		} catch (Exception e) {
			System.out.println(e);
		}
        return "redirect:/admin/branches";
    }
	@GetMapping("/add")
    public String displayAddBranchPage(HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request      
	 
	    model.addAttribute("branch",new BranchDTO());
        return "admin/update-branch";
    }
    @PostMapping("/add")
    public String addBranchPage(@ModelAttribute("branch") BranchDTO branch ,HttpSession session,Model model){
        //Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<BranchDTO> entity = new HttpEntity<>(branch,headers);
        try {
        	ResponseEntity<String> response = restTemplate.exchange(API_GET_BRANCHES,HttpMethod.POST, entity, String.class);
		} catch (Exception e) {
			System.out.println(e);
		}
	    
        return "redirect:/admin/branches";
    }
}
