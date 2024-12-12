package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.SeatDTO;

import java.util.List;

public interface ISeatService {
    List<SeatDTO>  getSeatsByScheduleIdAndUserId(Integer scheduleId,Integer userId);
    List<SeatDTO> getAllSeatByRoom(Integer roomId);
    void addAllSeat(List<SeatDTO> seats,Integer roomId);
    void updateSeats(List<SeatDTO> seats);
}
