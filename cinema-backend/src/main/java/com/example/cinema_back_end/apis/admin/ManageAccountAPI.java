 package com.example.cinema_back_end.apis.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cinema_back_end.dtos.BranchDTO;
import com.example.cinema_back_end.entities.User;
import com.example.cinema_back_end.security.service.IRoleService;
import com.example.cinema_back_end.security.service.IUserService;

@RestController
@RequestMapping("/api/admin/accounts")
public class ManageAccountAPI {
	@Autowired
	private IUserService accountService;
	@GetMapping
	public ResponseEntity<List<User>> getAllUser(){
		return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
	}
	@GetMapping("/paging")
	public ResponseEntity<List<User>> getPageUser(      
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
		    @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
		    @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){
	    Sort sortable = null;
	    if (sort.equals("ASC")) {
	      sortable = Sort.by("id").ascending();
	    }
	    if (sort.equals("DESC")) {
	      sortable = Sort.by("id").descending();
	    }
	    Pageable pageable = PageRequest.of(page, size, sortable);
		return new ResponseEntity<>(accountService.getPageUser(pageable), HttpStatus.OK);
	}
	@GetMapping("/count-page")
	public ResponseEntity<Long> getCountPage(      
		    @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){
		return new ResponseEntity<>(accountService.countPage(size), HttpStatus.OK);
	}
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@Param("userId") Integer userId){
    	accountService.remove(userId);
    	return new ResponseEntity<String>("Xóa tài khoản thành công!", HttpStatus.OK);
    }
	@PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user){
		try {
	    	accountService.saveUserByAdmin(user, true);
	        return  new ResponseEntity<String>("Thêm tài khoản thành công!" ,HttpStatus.OK);
		} catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

    }
    @PutMapping
    public ResponseEntity<String> updateUserAndCheckPassword(@RequestBody User user,@RequestParam String newPassword){
    	try {
        	if(newPassword!="") {
        		user.setPassword(newPassword);
        		accountService.saveUserByAdmin(user,true);
        	}else {
        		accountService.saveUserByAdmin(user,false);
        	}
        	return  new ResponseEntity<String>("Cập nhật tài khoản thành công!" ,HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

        
    }
}
