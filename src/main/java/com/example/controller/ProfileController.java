package com.example.controller;

import com.example.dto.NotificationDTO;
import com.example.model.Question;
import com.example.model.User;
import com.example.service.NotificationService;
import com.example.service.QuestionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {


    @Autowired
    QuestionService questionService;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                          Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("questions".contains(action)) {
            PageInfo<Question> pageInfo = questionService.findByPages(user, pageNum, pageSize);
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            model.addAttribute("pageInfo",pageInfo);
        } else if ("replies".contains(action)) {
            PageInfo<NotificationDTO> pageInfo = notificationService.list(user.getId(), pageNum, pageSize);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pageInfo",pageInfo);
        }
        return "profile";
    }
}
