package com.example.service;

import com.example.dto.CommentResponseDTO;
import com.example.dto.QuestionDTO;
import com.example.dto.QuestionQueryDTO;
import com.example.exception.CustomizeErrorCode;
import com.example.exception.CustomizeException;
import com.example.mapper.QuestionMapper;
import com.example.mapper.UserMapper;
import com.example.model.Question;
import com.example.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    public PageInfo<QuestionDTO> findByPages(String search, String tag, int pageNum, int pageSize) {
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
            questionQueryDTO.setSearch(search);
        }else {
            questionQueryDTO.setSearch(".");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionDTO> questionDTOList = questionMapper.selectBySearch(questionQueryDTO);
        return new PageInfo<>(questionDTOList);
    }

    public PageInfo<Question> findByPages(User user, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questionList = questionMapper.findByUserId(user.getId());
        return new PageInfo<>(questionList);
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.findById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }


    public void CreateOrUpdate(Question question) {
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            questionMapper.insert(question);
        } else {
            questionMapper.update(question);
        }
    }

    public void updateViewCount(Integer id) {
        questionMapper.updateViewCount(id);
    }

    public Question findById(Integer id) {
        return questionMapper.findById(id);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays
                .stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
