package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.BranchDTO;
import com.example.cinema_back_end.dtos.RoomDTO;
import com.example.cinema_back_end.entities.Room;
import com.example.cinema_back_end.repositories.IRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService implements IRoomService{
    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<RoomDTO> getRooms(Integer movieId, Integer branchId, String startDate, String startTime) {
        return roomRepository.getRoomByBranchAndMovieAndSchedule(movieId,branchId, LocalDate.parse(startDate), LocalTime.parse(startTime))
                .stream().map(room -> modelMapper.map(room,RoomDTO.class))
                .collect(Collectors.toList());
    }

	@Override
	public List<RoomDTO> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoomDTO getById(Integer id) {
		
		return modelMapper.map(roomRepository.getById(id),RoomDTO.class);
	}

	@Override
	public void update(RoomDTO room) {
		roomRepository.save(modelMapper.map(room, Room.class));
		
	}
	@Override
	public RoomDTO add(RoomDTO room) {
		return modelMapper.map(
				roomRepository.save(modelMapper.map(room,Room.class))
				,RoomDTO.class);
	}
	@Override
	public void remove(Integer id) {
		roomRepository.deleteById(id);
		
	}
	
	public List<RoomDTO> getRoomsByBranch(Integer branchId){
		return roomRepository.findRoomsByBranch_Id(branchId).stream()
				.map(room->modelMapper.map(room, RoomDTO.class)).collect(Collectors.toList());
	}
}
