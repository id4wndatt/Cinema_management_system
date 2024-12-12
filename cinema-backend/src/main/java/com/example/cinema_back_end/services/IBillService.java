package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.BillDTO;
import com.example.cinema_back_end.dtos.BookingRequestDTO;

public interface IBillService extends IGeneralService<BillDTO>{
    void createNewBill(BookingRequestDTO bookingRequestDTO) throws RuntimeException;
    
	
}
