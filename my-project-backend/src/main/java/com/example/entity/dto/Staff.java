package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_staff")
public class Staff {
    @TableId(type = IdType.AUTO)
    Integer id;
    String name;
    Integer gender;
    String department;
    Date time;
}
