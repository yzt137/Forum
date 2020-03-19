package com.example.service;

import com.example.dto.CommentResponseDTO;
import com.example.enums.CommentTypeEnum;
import com.example.enums.NotificationStatusEnum;
import com.example.enums.NotificationTypeEnum;
import com.example.exception.CustomizeErrorCode;
import com.example.exception.CustomizeException;
import com.example.mapper.CommentMapper;
import com.example.mapper.NotificationMapper;
import com.example.mapper.QuestionMapper;
import com.example.model.Comment;
import com.example.model.Notification;
import com.example.model.Question;
import com.example.model.User;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserService userService;

    @Autowired
    NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        //exception处理
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //核心业务
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            Comment dbComment = commentMapper.findById(comment.getParentId());
            if (dbComment != null) {
                commentMapper.insert(comment);
                commentMapper.updateCommentCount(comment.getParentId());
                createNotify(comment, dbComment, NotificationTypeEnum.REPLY_COMMENT);
            } else {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
        } else {
            Question question = questionMapper.findById(comment.getParentId());
            if (question != null) {
                commentMapper.insert(comment);
                questionMapper.updateCommentCount(question.getId());
                createNotify(comment, question, NotificationTypeEnum.REPLY_QUESTION);
            } else {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public List<CommentResponseDTO> listByTargetId(Integer id, CommentTypeEnum type) {
        ArrayList<CommentResponseDTO> dtos = new ArrayList<>();
        List<Comment> comments = commentMapper.findByQuestionId(id, type.getType());
        if (comments != null) {
            for (Comment comment : comments) {
                User user = userService.findById(comment.getCommentator());
                CommentResponseDTO dto = new CommentResponseDTO();
                BeanUtils.copyProperties(comment, dto);
                dto.setUser(user);
                dtos.add(dto);
            }
        }
        return dtos;
    }

    private void createNotify(Comment comment, Object object, NotificationTypeEnum notificationType) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        if (notificationType == NotificationTypeEnum.REPLY_COMMENT){
            notification.setReceiver(((Comment) object).getCommentator());
            notification.setOuterId(((Comment) object).getParentId());
            notification.setOuterTitle(((Comment) object).getContent());
        }else {
            notification.setReceiver(((Question) object).getCreator());
            notification.setOuterId(((Question) object).getId());
            notification.setOuterTitle(((Question) object).getTitle());
        }
        if (notification.getReceiver().equals(notification.getNotifier())){
            return;
        }
        notificationMapper.insert(notification);

    }
}
