package com.example.cinema_client.controllers;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.JwtResponseDTO;
import com.example.cinema_client.models.Role;
import com.example.cinema_client.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.*;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping
public class LoginController {
    @Autowired
    private RestTemplate restTemplate;

    public static String apiLogin = Api.baseURL+"/login";
    public static String API_REGISTER = Api.baseURL+"/register";
    
    @GetMapping("/login")
    public String loginPage(HttpSession session,Model model) {
    	model.addAttribute("user",new User());
        session.removeAttribute("jwtResponse");
        session.removeAttribute("admin");
        return "login";
    }
    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model) {
    	model.addAttribute("user",new User());
        session.removeAttribute("jwtResponse");
        session.removeAttribute("admin");
        return "register";
    }
    @PostMapping("/login")
    public String login(Model model, HttpServletRequest request, HttpSession session) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        JwtResponseDTO jwt;
        try {
            HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
            ResponseEntity<JwtResponseDTO> jwtResponse
                    = restTemplate.exchange(apiLogin, HttpMethod.POST, httpEntity, JwtResponseDTO.class);
            jwt=jwtResponse.getBody();
            session.setAttribute("jwtResponse", (JwtResponseDTO) jwtResponse.getBody());
        }
        catch (HttpClientErrorException ex){
            model.addAttribute("loginError",ex.getResponseBodyAsString());
            model.addAttribute("hasLoginErrors", true);
            model.addAttribute("user",user);
            System.out.println(ex);
            return "login";
        }
        for(Role role: jwt.getRoles()) {
        	if(role.equals("ROLE_ADMIN")) {
        		session.setAttribute("admin", true);
        		return "redirect:/admin";
        	}
        }
        	return "redirect:/"; 

    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            Set<Role> roles = new HashSet<Role>();
            Role roleClient = new Role();
            roleClient.setName("ROLE_CLIENT");
            roles.add(roleClient);
            user.setRoles(roles);
            try {
                HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
                ResponseEntity<JwtResponseDTO> jwtResponse
                        = restTemplate.exchange(API_REGISTER, HttpMethod.POST, httpEntity, JwtResponseDTO.class);
                request.getSession().setAttribute("jwtResponse", (JwtResponseDTO) jwtResponse.getBody());
            }catch (HttpClientErrorException ex){
                model.addAttribute("registerError",ex.getResponseBodyAsString());
                model.addAttribute("hasErrors", true);
                model.addAttribute("user",user);
                return "register";
            }
            return "redirect:/";
        }
    }

    @GetMapping("/sign-out")
    public String signOut(HttpSession session) {
        session.removeAttribute("jwtResponse");
        session.removeAttribute("admin");
        return "redirect:/";
    }
}
