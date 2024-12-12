package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.RoomDTO;

import java.util.List;

public interface IRoomService extends IGeneralService<RoomDTO>{
    List<RoomDTO> getRooms(Integer movieId,Integer branchId,String startDate,String startTime);
    List<RoomDTO> getRoomsByBranch(Integer branchId);
    RoomDTO add(RoomDTO room);
}
