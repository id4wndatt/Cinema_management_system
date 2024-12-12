package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.RoomDTO;
import com.example.cinema_back_end.dtos.SeatDTO;
import com.example.cinema_back_end.entities.Room;
import com.example.cinema_back_end.entities.Seat;
import com.example.cinema_back_end.repositories.IScheduleRepository;
import com.example.cinema_back_end.repositories.ISeatRepository;
import com.example.cinema_back_end.repositories.ITicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService implements ISeatService{
    @Autowired
    private ISeatRepository seatRepository;
    @Autowired
    private IScheduleRepository scheduleRepository;
    @Autowired
    private ITicketRepository ticketRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SeatDTO> getSeatsByScheduleIdAndUserId(Integer scheduleId,Integer userId) {
        // Lấy ra các chỗ ngồi của phòng trong lịch đó
        Room room = scheduleRepository.getById(scheduleId).getRoom();
        List<Seat> listSeat = seatRepository.getSeatByRoom_Id(room.getId());

        // Lấy ra các vé đã được đặt trong lịch đó rồi map sang các chỗ ngồi
        List<Seat> occupiedSeats = ticketRepository.findTicketsBySchedule_Id(scheduleId)
                .stream().map(ticket -> ticket.getSeat())
                .collect(Collectors.toList());
        // Lấy ra các vé người dùng đã đặt trong lịch đó rồi map sang các chỗ ngồi
        List<Seat> checkedSeats= ticketRepository.findTicketsByUserIdAndScheduleId(userId,scheduleId)
                .stream().map(ticket -> ticket.getSeat())
                .collect(Collectors.toList());
        // Map list chỗ ngồi của phòng ở bước 1 sang list dto
        List<SeatDTO> filteredSeats = listSeat.stream().map(seat -> {
           SeatDTO seatDTO = modelMapper.map(seat,SeatDTO.class);
           if(occupiedSeats.stream()
                   .map(occupiedSeat->occupiedSeat.getId())
                   .collect(Collectors.toList()).contains(seat.getId())){
               seatDTO.setIsOccupied(1); // Nếu ghế nào nằm trong list ghế đã được occupied thì set = 1
           }
           return seatDTO;
        }).collect(Collectors.toList());
        filteredSeats = filteredSeats.stream().map(seat -> {
            SeatDTO seatDTO = modelMapper.map(seat,SeatDTO.class);
            if(checkedSeats.stream()
                    .map(checkedSeat->checkedSeat.getId())
                    .collect(Collectors.toList()).contains(seat.getId())){
                seatDTO.setChecked(true); // Nếu ghế nào nằm trong list ghế đã được người dùng đặt thì set = true
            }
            return seatDTO;
         }).collect(Collectors.toList());
        return  filteredSeats;
    }

	@Override
	public List<SeatDTO> getAllSeatByRoom(Integer roomId) {
		return seatRepository.getSeatByRoom_Id(roomId).stream()
				.map(seat-> modelMapper.map(seat, SeatDTO.class)).collect(Collectors.toList());
	}

	@Override
	public void addAllSeat(List<SeatDTO> seats,Integer roomId) {
		seatRepository.saveAll(seats.stream().map(
				seat->{
						Seat seatEntity=modelMapper.map(seat, Seat.class);
						seatEntity.setRoom(new Room());
						seatEntity.getRoom().setId(roomId);
						return seatEntity;
					}).collect(Collectors.toList()));
	}

	@Override
	public void updateSeats(List<SeatDTO> seats) {
		seatRepository.saveAll(seats.stream().map(seat-> modelMapper.map(seat, Seat.class))
				.collect(Collectors.toList()));
	}
}
