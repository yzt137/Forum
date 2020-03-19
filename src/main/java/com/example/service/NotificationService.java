package com.example.service;

import com.example.dto.NotificationDTO;
import com.example.enums.NotificationStatusEnum;
import com.example.enums.NotificationTypeEnum;
import com.example.exception.CustomizeErrorCode;
import com.example.exception.CustomizeException;
import com.example.mapper.NotificationMapper;
import com.example.model.Notification;
import com.example.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    NotificationMapper notificationMapper;

    public PageInfo<NotificationDTO> list(Integer id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<NotificationDTO> notificationDTOList = notificationMapper.findAllByQuestionId(id);
        if (notificationDTOList != null) {
            for (NotificationDTO notificationDTO : notificationDTOList) {
                System.out.println(notificationDTO);
               notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notificationDTO.getType()));
            }
            return new PageInfo<>(notificationDTOList);
        }else {
            return null;
        }

    }

    public NotificationDTO read(Integer id, User user) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByNotifier(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

    public Integer unread(Integer id) {
        return notificationMapper.countByNotifier(id);
    }
}
