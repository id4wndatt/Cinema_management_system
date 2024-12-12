package com.example.cinema_back_end.apis;

import com.example.cinema_back_end.dtos.BranchDTO;
import com.example.cinema_back_end.repositories.IBranchRepository;
import com.example.cinema_back_end.services.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/branches", produces = "application/json")
public class BranchApi {
    @Autowired
    private IBranchService branchService;

    @GetMapping("/movie/branches-schedules")
    private ResponseEntity<List<BranchDTO>> getBranchesThatShowTheMovie(@RequestParam Integer movieId){
        return new ResponseEntity<>(branchService.getBranchesThatShowTheMovie(movieId), HttpStatus.OK);
    }
    @GetMapping("/branches-movies")
    private ResponseEntity<List<BranchDTO>> getAllBranchesAndMovies(){
        return new ResponseEntity<>(branchService.getBranchesAndSchedules(), HttpStatus.OK);
    }
	@GetMapping
	public ResponseEntity<List<BranchDTO>> getAllBranches(){
		return new ResponseEntity<>(branchService.findAll(),HttpStatus.OK);
	}
}
