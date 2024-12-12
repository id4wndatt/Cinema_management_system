package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.TicketDTO;

import java.util.List;

public interface ITicketService extends IGeneralService<TicketDTO>{
    List<TicketDTO> getTicketsByUserId(Integer userId);

	List<TicketDTO> getTicketsByBillId(Integer billId);
}
