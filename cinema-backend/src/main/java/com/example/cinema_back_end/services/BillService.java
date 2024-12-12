package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.BillDTO;
import com.example.cinema_back_end.dtos.BookingRequestDTO;

import com.example.cinema_back_end.entities.Bill;
import com.example.cinema_back_end.entities.Schedule;
import com.example.cinema_back_end.entities.Ticket;
import com.example.cinema_back_end.entities.User;
import com.example.cinema_back_end.repositories.IBillRepository;
import com.example.cinema_back_end.repositories.IScheduleRepository;
import com.example.cinema_back_end.repositories.ISeatRepository;
import com.example.cinema_back_end.repositories.ITicketRepository;
import com.example.cinema_back_end.security.repo.IUserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService implements IBillService{
    @Autowired
    private IScheduleRepository scheduleRepository;
    @Autowired
    private ITicketRepository ticketRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ISeatRepository seatRepository;
    @Autowired
    private IBillRepository billRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    @Transactional
    public void createNewBill(BookingRequestDTO bookingRequestDTO) throws RuntimeException {
		LocalDate date=LocalDate.now();
		LocalTime time=LocalTime.now();
        //Lấy ra lịch
        Schedule schedule = scheduleRepository.getById(bookingRequestDTO.getScheduleId());
        if(schedule.getStartDate().compareTo(date)>0 ||
				(schedule.getStartDate().compareTo(date)==0 && schedule.getStartTime().compareTo(time)>0)	) {
	        //Lấy ra người dùng
	        User user = userRepository.getById(bookingRequestDTO.getUserId());
	
	        //Lưu Bill gồm thông tin người dùng xuống trước
	        Bill billToCreate = new Bill();
	        billToCreate.setUser(user);
	        billToCreate.setCreatedTime(LocalDateTime.now());
	        Bill createdBill = billRepository.save(billToCreate);
	
	        //Với mỗi ghế ngồi check xem đã có ai đặt chưa, nếu rồi thì throw, roll back luôn còn ko
	        //thì đóng gói các thông tin ghế và lịch vào vé và lưu xuống db
	        bookingRequestDTO.getListSeatIds().forEach(seatId->{
	            if(!ticketRepository.findTicketsBySchedule_IdAndSeat_Id(schedule.getId(),seatId)
	                    .isEmpty()){// Nếu đã có người đặt vé ghế đó ở lịch cụ thể đó thì
	                throw new RuntimeException("Đã có người nhanh tay hơn đặt ghế, mời bạn chọn lại!");
	            }
	            // đóng gói lịch, seat và bill vào từng vé rồi add vào list vé
	            Ticket ticket = new Ticket();
	            ticket.setSchedule(schedule);
	            ticket.setSeat(seatRepository.getById(seatId));
	            ticket.setBill(createdBill);
	            ticket.setQrImageURL("https://scontent-sin6-2.xx.fbcdn.net/v/t1.15752-9/268794058_655331555823095_3657556108194277679_n.png?_nc_cat=105&ccb=1-5&_nc_sid=ae9488&_nc_ohc=BrNXGO8HufkAX_OGjWc&_nc_ht=scontent-sin6-2.xx&oh=03_AVK_zaJj7pziY9nLrVqoIQJAzbomu4KPgED1PxFFpYfCrQ&oe=61F778D8");
	            ticketRepository.save(ticket);
	        });
        }else {
        	throw new RuntimeException("lịch chiếu đã kết thúc không thể đặt chỗ ngồi!");
        }
    }

	@Override
	public List<BillDTO> findAll() {
		// TODO Auto-generated method stub
		return billRepository.findAll().stream().map(bill->modelMapper.map(bill, BillDTO.class)).collect(Collectors.toList());
	}

	@Override
	public BillDTO getById(Integer billId) {
		// TODO Auto-generated method stub
		 return modelMapper.map(billRepository.getById(billId),BillDTO.class);
	}	
	

	@Override
	public void remove(Integer id) {
		// TODO Auto-generated method stub
		billRepository.deleteById(id);
	}

	@Override
	public void update(BillDTO t) {
		// TODO Auto-generated method stub
		
	}
}
