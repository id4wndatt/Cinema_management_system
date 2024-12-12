package com.example.cinema_client.controllers;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.JwtResponseDTO;
import com.example.cinema_client.models.TicketDTO;
import com.example.cinema_client.models.User;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
	@Autowired
	private RestTemplate restTemplate;
	
	public static String API_FORGOT_PASSWORD = Api.baseURL+"/api/forgot-password";
	public static String API_VALIDATE_TOKEN_PASSWORD = Api.baseURL+"/api/forgot-password/validate-token";
	@GetMapping
    public String displayPage(Model model, HttpServletRequest request){
		return "forgot-password";
    }
	@PostMapping
    public String sendPage(@RequestParam String email,Model model, HttpServletRequest request){
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(API_FORGOT_PASSWORD+"?email="+email,String.class);
            model.addAttribute("status", 1);
            model.addAttribute("message", response.getBody());
		} catch (Exception e) {
			model.addAttribute("status", 0);
			model.addAttribute("message", e.getMessage());
		}
        return "forgot-password";
    }
	@GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token,@RequestParam String email
    		,Model model, HttpServletRequest request){
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(API_VALIDATE_TOKEN_PASSWORD+"?token="+token,String.class);
            model.addAttribute("status", 1);
            model.addAttribute("message", response.getBody());
		} catch (Exception e) {
			System.out.println(e);
			model.addAttribute("status", 0);
			model.addAttribute("message", e.getMessage());
			return "/forgot-password";
		}
		model.addAttribute("token",token);
		model.addAttribute("email",email);
        return "reset-password";
    }
	@PostMapping("/reset-password")
    public String validatePassword(@RequestParam String token,@RequestParam String password
    		,Model model, HttpServletRequest request){
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_FORGOT_PASSWORD)
                .queryParam("token", "{token}")
                .queryParam("password", "{password}")
                .encode()
                .toUriString();
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("password", password);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(urlTemplate,null,String.class, params);
            model.addAttribute("status", 1);
            model.addAttribute("message", response.getBody());
		} catch (Exception e) {
			model.addAttribute("status", 0);
			model.addAttribute("message", e.getMessage());
		}
		return "reset-password";

    }
}
