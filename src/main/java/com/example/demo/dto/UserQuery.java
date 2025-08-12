package com.example.demo.dto;

import lombok.Data;
//分页要求
@Data
public class UserQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String username;
    private String email;
    private Integer status;
}