package com.example.model;

import lombok.Data;

@Data
public class Notification {
    private Integer id;
    private Integer outerId;
    private Integer type;
    private Long gmtCreate;
    private Integer notifier;
    private Integer receiver;
    private Integer status;
    private String outerTitle;
}
