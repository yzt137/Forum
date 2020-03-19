package com.example.controller;

import com.example.dto.QuestionDTO;
import com.example.service.QuestionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

    @Autowired
    QuestionService questionService;


    @GetMapping({"/index", "/"})
    public String index(Model model,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        PageInfo<QuestionDTO> pageInfo = questionService.findByPages(search,tag,pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);
        return "index";
    }
}
