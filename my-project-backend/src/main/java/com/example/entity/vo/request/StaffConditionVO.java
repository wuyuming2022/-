package com.example.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@AllArgsConstructor
public class StaffConditionVO {
    String name;
    Integer gender;
    String department;
    int page;
}
