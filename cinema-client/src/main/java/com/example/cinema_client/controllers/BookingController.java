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
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.cinema_client.constants.Api;
import com.example.cinema_client.models.JwtResponseDTO;
import com.example.cinema_client.models.ScheduleDTO;
import com.example.cinema_client.models.SeatDTO;


@Controller
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private RestTemplate restTemplate;

    public static String API_GET_SCHEDULE=Api.baseURL+"/api/schedule";
    public static String API_GET_SEATS= Api.baseURL+"/api/seats";

    @GetMapping("/{scheduleId}")
    public String displayRoomAndSeat(@PathVariable("scheduleId") Integer scheduleId,HttpServletRequest request,Model model) {
        HttpSession session = request.getSession();
        // Gắn access token jwt vào header để gửi kèm request
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        JwtResponseDTO jwtResponseDTO = (JwtResponseDTO)session.getAttribute("jwtResponse");
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+jwtResponseDTO.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Gọi api lấy ra lịch được chọn
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_SCHEDULE)
                .queryParam("scheduleId", "{scheduleId}")
                .encode()
                .toUriString();
        Map<String,String> listRequestParam = new HashMap<>();
        listRequestParam.put("scheduleId", scheduleId.toString());

        ResponseEntity<ScheduleDTO> response = restTemplate.exchange(urlTemplate, HttpMethod.GET,entity,ScheduleDTO.class
        ,listRequestParam);
        model.addAttribute("schedule",response.getBody());
        
     // Gọi api lấy ra chỗ ngồi của phòng
        urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_SEATS)
                .queryParam("scheduleId", "{scheduleId}")
                .queryParam("userId", "{userId}")
                .encode()
                .toUriString();
        listRequestParam.put("userId", jwtResponseDTO.getId().toString());
        ResponseEntity<SeatDTO[]> responseSeats = restTemplate.exchange(urlTemplate, HttpMethod.GET,entity,SeatDTO[].class
        ,listRequestParam);
        model.addAttribute("seats",responseSeats.getBody());
    	return "client/booking-seat";
    }
    
}
