package com.example.cinema_client.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.cinema_client.models.JwtResponseDTO;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
	@GetMapping
	public String displayError(HttpSession session) {
		JwtResponseDTO jwt=(JwtResponseDTO) session.getAttribute("jwtResponse");
		if(jwt==null) {
			return "redirect:/login";
		}
		else {
			if(jwt.getRoles().contains("ROLE_ADMIN")){
				return "redirect:/admin";
			}else {
				return "redirect:/";
			}
		}
	}
}
