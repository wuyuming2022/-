package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class StaffPreViewVO {
    Integer id;
    String name;
    Integer gender;
    String department;
    Date time;
}
