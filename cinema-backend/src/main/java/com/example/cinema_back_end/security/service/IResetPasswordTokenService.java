package com.example.cinema_back_end.security.service;

import javax.security.auth.login.AccountNotFoundException;

import com.example.cinema_back_end.entities.ResetPasswordToken;
import com.example.cinema_back_end.entities.User;

public interface IResetPasswordTokenService {
	public ResetPasswordToken generateResetPasswordToken(String email) throws AccountNotFoundException;
	public ResetPasswordToken getByToken(String token);
	public void remove(Integer id);
}
