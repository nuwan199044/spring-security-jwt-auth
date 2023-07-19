package com.myapp.springsecurity.jwtauth.dto;

import lombok.Data;

@Data
public class RoleToUserDTO {

    private String userName;
    private String roleName;

}
