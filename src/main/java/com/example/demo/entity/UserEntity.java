package com.example.demo.entity;

import lombok.Data;

@Data
public class UserEntity {
	
    private Integer userId;
    
    private String userName;
    
    private String password;
    
    private String email;
    
    private String address;
}
