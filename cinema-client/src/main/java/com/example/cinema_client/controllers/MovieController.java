package com.example.cinema_client.controllers;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.BranchDTO;
import com.example.cinema_client.models.MovieDTO;
import com.example.cinema_client.models.ScheduleDTO;
import com.example.cinema_client.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("movies")
public class MovieController {
    @Autowired
    private RestTemplate restTemplate;

    public static String API_GET_MOVIE_DETAILS = Api.baseURL+"/api/movies/details";
    public static String API_GET_All_MOVIES = Api.baseURL+"/api/movies";
    public static String API_GET_BRANCHES_BY_MOVIE = Api.baseURL+"/api/branches/movie/branches-schedules";
    public static String API_GET_MOVIES_BY_NAME = Api.baseURL+"/api/movies/search";
    @GetMapping("/detail")
    public String displayMovieDetailPage(@RequestParam Integer movieId, Model model){
        // Truyền tham số movieId vào query string rồi gửi request
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_MOVIE_DETAILS)
                .queryParam("movieId", "{movieId}")
                .encode()
                .toUriString();
        Map<String, Integer> params = new HashMap<>();
        params.put("movieId", movieId);
        ResponseEntity<MovieDTO> response = restTemplate.getForEntity(urlTemplate,MovieDTO.class,params);
        MovieDTO movie = response.getBody();
        // Truyền tham số movieId vào query string rồi gửi request
        urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_BRANCHES_BY_MOVIE)
                .queryParam("movieId", "{movieId}")
                .encode()
                .toUriString();

        HttpEntity<BranchDTO[]> responseBranches = restTemplate.getForEntity(urlTemplate,BranchDTO[].class,params);
        BranchDTO[] branchDTOS = responseBranches.getBody();
        model.addAttribute("branches",branchDTOS);
        model.addAttribute("movie",movie);
        return "movie-details";
    }
    @GetMapping
    public String displayAllMovies( Model model){
        ResponseEntity<MovieDTO[]> response = restTemplate.getForEntity(API_GET_All_MOVIES,MovieDTO[].class);
        MovieDTO[] movies = response.getBody();
        model.addAttribute("movies",movies);
        return "movies";
    }
    @PostMapping
    public String searchMoviesByName(@RequestParam String search,@RequestParam String status,HttpServletRequest request, Model model){
        // Gọi api lấy ra lịch được chọn
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_MOVIES_BY_NAME)
                .queryParam("name", "{name}")
                .queryParam("status", "{status}")
                .encode()
                .toUriString();
        Map<String,String> listRequestParam = new HashMap<>();
        listRequestParam.put("name", search);
        listRequestParam.put("status", status);
        ResponseEntity<MovieDTO[]> response = restTemplate.getForEntity(urlTemplate,MovieDTO[].class,listRequestParam);
        MovieDTO[] movies = response.getBody();
        model.addAttribute("status",status);
        model.addAttribute("movies",movies);
        return "movies";
    }
}
