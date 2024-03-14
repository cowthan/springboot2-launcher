package com.danger.t7.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.danger.t7.entity.UserEntity;
import com.danger.t7.models.UserRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * message
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * findList
     * @param form form
     * @return result
     */
    List<UserEntity> findList(@Param("form") UserRequest form);

    /**
     * findById
     * @param id id
     * @return result
     */
    @Select("select * from user where id = #{id}")
    UserEntity findById(Long id);

    /**
     * updateStatus
     * @param id id
     * @param status status
     * @return result
     */
    @Update("update user set status = #{status} where id = #{id}")
    int updateStatus(Long id, Integer status);
}
