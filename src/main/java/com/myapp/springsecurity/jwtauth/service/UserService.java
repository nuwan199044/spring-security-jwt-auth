package com.myapp.springsecurity.jwtauth.service;

import com.myapp.springsecurity.jwtauth.entity.Role;
import com.myapp.springsecurity.jwtauth.entity.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String userName, String roleName);
    User getUser(String userName);
    List<User> getUsers();

}
