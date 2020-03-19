package com.example.mapper;

import com.example.dto.QuestionDTO;
import com.example.dto.QuestionQueryDTO;
import com.example.model.Question;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,comment_count,view_count,like_count,tag) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{viewCount},#{likeCount},#{tag})")
    void insert(Question question);

    @Select("select * from question order by gmt_Create DESC")
    String findTitleById();

    @Select("select * from question where creator=#{userId} order by gmt_Create DESC")
    List<Question> findByUserId(Integer userId);

    @Select("select *from question where id=#{id}")
    Question findById(Integer id);

    @Update("update question set title=#{title}, description=#{description},tag=#{tag}, gmt_modified=#{gmtModified} where id=#{id}")
    void update(Question question);

    @Update("update question set view_count=view_count+1 where id = #{id}")
    void updateViewCount(Integer id);

    @Update("update question set comment_count=comment_count+1 where id = #{id}")
    void updateCommentCount(Integer id);

    @Select("select *from question where id!=#{id} and tag regexp #{tag}")
    List<Question> selectRelated(Question question);

    @Select("select *from question where title regexp #{search} order by gmt_Create DESC ")
    @Results({
            @Result(column="creator",property="user",one=@One(select="com.example.mapper.UserMapper.findById",fetchType= FetchType.EAGER))
    })
    List<QuestionDTO> selectBySearch(QuestionQueryDTO questionQueryDTO);

}
