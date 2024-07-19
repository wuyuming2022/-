package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentVO {
    int id;
    String content;
    Date time;
    String quote;
}
