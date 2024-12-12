package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.MovieDTO;
import com.example.cinema_back_end.dtos.ScheduleDTO;
import com.example.cinema_back_end.entities.Movie;
import com.example.cinema_back_end.entities.Schedule;
import com.example.cinema_back_end.repositories.IMovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService implements IMovieService{

    @Autowired
    private IMovieRepository  movieRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<MovieDTO> findAllShowingMovies() {
        return movieRepository.findMoviesByIsShowingOrderByIdDesc(1)
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public MovieDTO getById(Integer movieId) {
        return modelMapper.map(movieRepository.getById(movieId),MovieDTO.class);
    }

    @Override
    public List<MovieDTO> findAllMoviesByNameAndStatus(String keyword,Integer status) {
    	if(status!=null) {
            return movieRepository.findMoviesByIsShowingAndNameContaining(status,keyword)
                    .stream().map(movie -> modelMapper.map(movie,MovieDTO.class))
                    .collect(Collectors.toList());
    	}
        return movieRepository.findMoviesByNameContaining(keyword)
                .stream().map(movie -> modelMapper.map(movie,MovieDTO.class))
                .collect(Collectors.toList());
    }
	@Override
	public MovieDTO getMovieAndSchedulesIsShowing(Integer id) {
		Movie movie= movieRepository.findById(id).get();
		MovieDTO movieDTO=modelMapper.map(movie, MovieDTO.class);
		LocalDate date=LocalDate.now();
		LocalTime time=LocalTime.now();
		List<ScheduleDTO> schedules=new LinkedList<>();
		for (Schedule schedule: movie.getScheduleList()) {
			if(schedule.getStartDate().compareTo(date)>0) {
				schedules.add(modelMapper.map(schedule,ScheduleDTO.class));
			}
			else if(schedule.getStartDate().compareTo(date)==0 && schedule.getStartTime().compareTo(time)>0) {
				schedules.add(modelMapper.map(schedule,ScheduleDTO.class));
			}
		}
        movieDTO.setSchedules(schedules);
        return movieDTO;
	}
	@Override
	public MovieDTO getMovieAndSchedules(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<MovieDTO> findAllComingMovies() {
        return movieRepository.findMoviesByIsShowingOrderByIdDesc(0)
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
	}
	@Override
	public List<MovieDTO> findAll() {
		return movieRepository.findAll().stream().map(movie->modelMapper.map(movie, MovieDTO.class)).collect(Collectors.toList());
	}
	@Override
	public void update(MovieDTO movie) {
		movieRepository.save(modelMapper.map(movie, Movie.class));
	}
	@Override
	public void remove(Integer id) {
		movieRepository.deleteById(id);
		
	}
}
