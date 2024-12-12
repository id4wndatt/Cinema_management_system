package com.example.cinema_client.controllers;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.BranchDTO;
import com.example.cinema_client.models.JwtResponseDTO;
import com.example.cinema_client.models.User;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/branches")
public class BranchController {
    @Autowired
    private RestTemplate restTemplate;

    public static String apiGetBranches = Api.baseURL+"/api/branches";

    @GetMapping
    public String displayBranchesPage( Model model, HttpServletRequest request){
        ResponseEntity<BranchDTO[]> response = restTemplate.getForEntity(apiGetBranches,BranchDTO[].class);
        BranchDTO[] branchDTOS = response.getBody();
        model.addAttribute("branches",branchDTOS);
        return "cluster-cinema";

    }
}
