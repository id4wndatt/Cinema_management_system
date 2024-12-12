package com.example.cinema_back_end.security.controller;



import com.example.cinema_back_end.entities.Role;
import com.example.cinema_back_end.entities.User;
import com.example.cinema_back_end.security.jwt.JwtResponse;
import com.example.cinema_back_end.security.jwt.JwtService;
import com.example.cinema_back_end.security.service.IRoleService;
import com.example.cinema_back_end.security.service.IUserService;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IUserService userService;
    
    @Autowired
    private IRoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.findByUsername(user.getUsername()).get();
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), currentUser.getFullName()
            		,currentUser.getRoles()));
        }
        catch (Exception e){
            System.out.println(e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sai email hoặc mật khẩu!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if(userService.findByUsername(user.getUsername()).isPresent()){
                throw new Exception("Đã tồn tại người dùng, vui lòng chọn tên đăng nhập khác");
            }
            String password = user.getPassword();
            //get roles in database by name to set for user
            Set<Role> roles= user.getRoles().stream().map(role ->
            {
            	Role existingRole=roleService.findByName(role.getName());
            	if(existingRole!=null) {
            		return existingRole;
            	}else {
            		return role;
            	}
            }).collect(Collectors.toSet());
            user.setRoles(roles);
            userService.save(user);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(),password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.findByUsername(user.getUsername()).get();
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), currentUser.getFullName()
            		,currentUser.getRoles()));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            
        }

    }
}