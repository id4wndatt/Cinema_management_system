package com.example.cinema_back_end.security.service;


import com.example.cinema_back_end.entities.Role;


public interface IRoleService extends IGeneralService<Role> {
    Role findByName(String name);
}