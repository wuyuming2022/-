package com.example.entity.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class TopicDetailVO {
    @TableId(type = IdType.AUTO)
    Integer id;
    String title;
    String content;
    Integer type;
    Date time;
    User user;
    Interact interact;

    @Data
    @AllArgsConstructor
    public static class Interact {
        Boolean like;
        Boolean collect;
    }

    @Data
    public static class User {
        Integer id;
        String username;
        String avatar;
        String desc;
        Integer gender;
        String qq;
        String wx;
        String phone;
        String email;
    }

}
