package com.example.cinema_back_end.security.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cinema_back_end.entities.ResetPasswordToken;
import com.example.cinema_back_end.entities.User;
import com.example.cinema_back_end.security.repo.IResetPasswordTokenRepository;
import com.example.cinema_back_end.security.repo.IUserRepository;

import net.bytebuddy.utility.RandomString;
@Service
public class ResetPasswordTokenService implements IResetPasswordTokenService{
	
	@Autowired 
	private IResetPasswordTokenRepository resetPasswordTokenRepository;
	
	@Autowired
	private IUserRepository userRepository;
	@Override
	public ResetPasswordToken generateResetPasswordToken(String email) throws AccountNotFoundException {
		//Kiểm tra email có tồn tại hay chưa
		User user = userRepository.findByUsername(email).orElse(null);
		if (user!= null) {
			String token = RandomString.make(30);
			ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByUser_id(user.getId()).orElse(null);
			if(resetPasswordToken==null) {
				resetPasswordToken=new ResetPasswordToken(token, user);
			}else {
				Integer id=resetPasswordToken.getId();
				resetPasswordToken=new ResetPasswordToken(token, user);
				resetPasswordToken.setId(id);
			}
			return resetPasswordTokenRepository.save(resetPasswordToken);
		} else {
			throw new AccountNotFoundException("Could not find any user with the email " + email);
		}
	}

	@Override
	public ResetPasswordToken getByToken(String token) {
		return resetPasswordTokenRepository.findByToken(token);
	}

	@Override
	public void remove(Integer id) {
		resetPasswordTokenRepository.deleteById(id);
		
	}
}
