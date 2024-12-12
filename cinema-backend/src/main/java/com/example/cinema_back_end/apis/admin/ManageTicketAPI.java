package com.example.cinema_back_end.apis.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cinema_back_end.dtos.TicketDTO;
import com.example.cinema_back_end.services.BillService;
import com.example.cinema_back_end.services.ITicketService;
@RestController
@RequestMapping("/api/admin/tickets")
public class ManageTicketAPI {
    @Autowired
    private ITicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets(){
        return new ResponseEntity<>(ticketService.findAll(), HttpStatus.OK);
    }
	@PostMapping
	public ResponseEntity<List<TicketDTO>> getTicketsBybill(@Param("billId") Integer billId) {
		return new ResponseEntity<List<TicketDTO>>(ticketService.getTicketsByBillId(billId), HttpStatus.OK);
	}
    @DeleteMapping
	public ResponseEntity<String> deleteTicket(@Param("ticketId") Integer ticketId) {
		ticketService.remove(ticketId);
		return new ResponseEntity<String>("Xóa vé thành công!", HttpStatus.OK);
	}
}