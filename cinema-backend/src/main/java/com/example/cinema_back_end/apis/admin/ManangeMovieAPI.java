package com.example.cinema_back_end.apis.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cinema_back_end.dtos.MovieDTO;
import com.example.cinema_back_end.services.IMovieService;

@RestController
@RequestMapping("/api/admin/movies")
public class ManangeMovieAPI {
	@Autowired
	private IMovieService movieService;
	@GetMapping
	public ResponseEntity<List<MovieDTO>> getAllMovie() {
		return new ResponseEntity<>(movieService.findAll(),HttpStatus.OK);
	}
	@PostMapping
    public ResponseEntity<String> addMovie(@RequestBody MovieDTO movie){
    	movieService.update(movie);
        return  new ResponseEntity<String>("Thêm phim thành công!" ,HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<String> updateMovie(@RequestBody MovieDTO movie){
    	movieService.update(movie);
        return  new ResponseEntity<String>("Cập nhật phim thành công!" ,HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteMovie(@Param("movieId") Integer movieId){
    	movieService.remove(movieId);
    	return new ResponseEntity<String>("Xóa phim thành công!", HttpStatus.OK);
    }
}
