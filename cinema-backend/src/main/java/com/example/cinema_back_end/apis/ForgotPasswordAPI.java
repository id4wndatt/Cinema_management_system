package com.example.cinema_back_end.apis;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cinema_back_end.constants.ClientURL;
import com.example.cinema_back_end.entities.ResetPasswordToken;
import com.example.cinema_back_end.entities.User;
import com.example.cinema_back_end.security.service.IResetPasswordTokenService;
import com.example.cinema_back_end.security.service.IUserService;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPasswordAPI {
    @Autowired
    private JavaMailSender mailSender;
    final private static String BASE_URL_RESET_PASSWORD=ClientURL.baseURL+"/forgot-password/reset-password";
    @Autowired
    private IResetPasswordTokenService resetPasswordTokenService;
    
    @Autowired
    private IUserService userService;
    
    
    @PostMapping
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String password){
    	ResetPasswordToken rptoken=resetPasswordTokenService.getByToken(token);
    	if(rptoken!=null) {
        	Date date = new Date();
        	if(rptoken.getExpiryDate().compareTo(date)>0) {
    	        try {
    	        	User user=rptoken.getUser();
    	            userService.changePassword(user.getId(), password);
    	            resetPasswordTokenService.remove(rptoken.getId());
    	            return new ResponseEntity<>("Đã đổi mật khẩu thành công !", HttpStatus.OK);
    	        } catch (RuntimeException e) {
    	            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    	        }
            }else {
            	return new ResponseEntity<>("Mã xác thực không còn hợp lệ!", HttpStatus.EXPECTATION_FAILED);
            }
    	}else {
    		return new ResponseEntity<>("Mã xác thực không hợp lệ!", HttpStatus.EXPECTATION_FAILED);
    	}
    }
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token){
    	ResetPasswordToken rptoken=resetPasswordTokenService.getByToken(token);
    	if(rptoken!=null) {
        	Date date = new Date();
        	if(rptoken.getExpiryDate().compareTo(date)>0) {
        		return new ResponseEntity<>("Mã xác thực hợp lệ!", HttpStatus.OK);
            }else {
            	return new ResponseEntity<>("Mã xác thực không còn hợp lệ!", HttpStatus.EXPECTATION_FAILED);
            }
    	}else {
    		return new ResponseEntity<>("Mã xác thực không hợp lệ!", HttpStatus.EXPECTATION_FAILED);
    	}
    }
    @GetMapping
    public ResponseEntity<String> sendResetPasswordToken(@RequestParam String email)  {
    	
        try {
        	ResetPasswordToken token=resetPasswordTokenService.generateResetPasswordToken(email);
        	sendEmail(email,BASE_URL_RESET_PASSWORD+"?token="+token.getToken()+"&email="+email);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        } catch (UnsupportedEncodingException e) {
        	return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		} catch (MessagingException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
        return new ResponseEntity<>("Đã gửi mã xác thực thành công !", HttpStatus.OK);
    }
    
    public void sendEmail(String recipientEmail,String data)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();              
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom("contact@ApMovie.com", "Ap Movie Support");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
         
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + data+ "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
         
        helper.setSubject(subject);
         
        helper.setText(content, true);
         
        mailSender.send(message);
    }
}
