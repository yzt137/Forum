package com.example.dto;

import com.example.model.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionDTO implements Serializable{
        private Integer id;
        private String title;
        private String description;
        private String tag;
        private Long gmtCreate;
        private Long gmtModified;
        private Integer creator;
        private Integer viewCount;
        private Integer likeCount;
        private Integer commentCount;
        private User user;
}
