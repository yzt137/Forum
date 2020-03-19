package com.example.controller;

import com.example.dto.CommentResponseDTO;
import com.example.dto.QuestionDTO;
import com.example.enums.CommentTypeEnum;
import com.example.service.CommentService;
import com.example.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> related = questionService.selectRelated(questionDTO);
        questionService.updateViewCount(id);
        List<CommentResponseDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions", related);
        return "question";
    }
}
