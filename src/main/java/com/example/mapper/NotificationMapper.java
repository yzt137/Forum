package com.example.mapper;

import com.example.dto.NotificationDTO;
import com.example.model.Notification;
import com.example.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NotificationMapper {

    @Select("select *from notification where notifier=#{notifier} order by gmt_Create DESC")
    List<NotificationDTO> findAllByQuestionId(Integer notifier);

    @Insert("insert into notification(outer_id,notifier,receiver,gmt_create,type,status,outer_title) values(#{outerId},#{notifier},#{receiver},#{gmtCreate},#{type},#{status},#{outerTitle})")
    void insert(Notification notification);

    @Delete("delete from user where id=#{id}")
    void delete(Integer id);

    @Select("select count(*) from notification where notifier=#{notifier} and status=0")
    Integer countByNotifier(Integer notifier);

    @Select("select * from notification where id=#{id}")
    Notification selectById(Integer id);

    @Update("update notification set status=#{status} where id=#{id}")
    void updateByNotifier(Notification notification);
}
