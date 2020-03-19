package com.example.controller;

import com.example.cache.TagCache;
import com.example.mapper.QuestionMapper;
import com.example.mapper.UserMapper;
import com.example.model.Question;
import com.example.model.User;
import com.example.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {


    @Autowired
    QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String publish(@PathVariable("id") Integer id, Model model) {
        Question question = questionService.findById(id);
        model.addAttribute("question", question);
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Question question, Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("tags", TagCache.get());
        if (StringUtils.isBlank(question.getTitle())) {
            model.addAttribute("error", "标题为空");
            return "publish";
        }
        if (StringUtils.isBlank(question.getDescription())) {
            model.addAttribute("error", "内容为空");
            return "publish";
        }
        if (StringUtils.isBlank(question.getTag())) {
            model.addAttribute("error", "标签为空");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(question.getTag());
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }
        model.addAttribute("question", question);

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        } else {
            question.setGmtModified(System.currentTimeMillis());
            question.setLikeCount(0);
            question.setCommentCount(0);
            question.setViewCount(0);
            questionService.CreateOrUpdate(question);
            return "redirect:/";
        }

    }
}
