package com.example.cinema_back_end.security.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cinema_back_end.entities.ResetPasswordToken;

public interface IResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken , Integer> {
	ResetPasswordToken findByToken(String token);
	Optional<ResetPasswordToken> findByUser_id(Integer userId);
}
