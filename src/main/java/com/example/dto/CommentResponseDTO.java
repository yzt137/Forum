package com.example.dto;

import com.example.model.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentResponseDTO implements Serializable {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Long likeCount;
    private Long gmtCreate;
    private Long gmtModified;
    private String content;
    private Integer commentator;
    private User user;
    private Integer commentCount;
}
