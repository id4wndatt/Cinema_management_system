package com.example.cinema_back_end.services;

import com.example.cinema_back_end.dtos.BranchDTO;

import java.util.List;

public interface IBranchService extends IGeneralService<BranchDTO>{
    List<BranchDTO> getBranchesThatShowTheMovie(Integer movieId);
    BranchDTO getBranchAndSchedules(Integer branchId);
    List<BranchDTO> getBranchesAndSchedules();
}
