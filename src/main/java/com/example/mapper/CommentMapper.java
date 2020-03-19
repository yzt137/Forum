package com.example.mapper;

import com.example.enums.CommentTypeEnum;
import com.example.model.Comment;
import com.example.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    @Insert("insert into comment(parent_id,type,commentator,like_count,content,gmt_create,gmt_modified)" +
            "values(#{parentId},#{type},#{commentator},#{likeCount},#{content},#{gmtCreate},#{gmtModified})")
    void insert(Comment comment);

    @Select("select * from comment where id=#{parentId}")
    Comment findById(Integer parentId);

    @Select("select * from comment")
    List<Comment> findAll();

    @Select("select * from comment where parent_id=#{parentId} and type=#{type}")
    List<Comment> findByQuestionId(Integer parentId,Integer type);

    @Update("update comment set comment_count=comment_count+1 where id=#{id}")
    void updateCommentCount(Integer id);
}
