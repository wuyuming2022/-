package com.example.entity.vo.response;

import com.example.entity.BaseData;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationVO{
    Integer id;
    String title;
    String content;
    String type;
    String url;
    Date time;
}
