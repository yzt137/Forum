package com.example.model;

import lombok.Data;

@Data
public class Comment {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Long likeCount;
    private Long gmtCreate;
    private Long gmtModified;
    private String content;
    private Integer commentator;
    private Integer commentCount;
}
