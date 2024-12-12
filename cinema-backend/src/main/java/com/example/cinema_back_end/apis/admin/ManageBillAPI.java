package com.example.cinema_back_end.apis.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cinema_back_end.dtos.BillDTO;
import com.example.cinema_back_end.dtos.BranchDTO;
import com.example.cinema_back_end.dtos.TicketDTO;
import com.example.cinema_back_end.services.IBillService;

@RestController
@RequestMapping("/api/admin/bills")
public class ManageBillAPI {
	@Autowired
	private IBillService billService;

	@GetMapping
	public ResponseEntity<List<BillDTO>> getAllBills() {
		return new ResponseEntity<>(billService.findAll(), HttpStatus.OK);
	}


	@DeleteMapping
	public ResponseEntity<String> deleteMovie(@Param("billId") Integer billId) {
		billService.remove(billId);
		return new ResponseEntity<String>("Xóa hóa đơn thành công!", HttpStatus.OK);
	}
}
