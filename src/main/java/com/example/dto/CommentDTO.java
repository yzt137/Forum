package com.example.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {
    private Integer parentId;
    private String content;
    private Integer type;
}
