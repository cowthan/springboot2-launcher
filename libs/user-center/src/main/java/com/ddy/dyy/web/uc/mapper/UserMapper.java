package com.ddy.dyy.web.uc.mapper;

import java.util.List;

import com.ddy.dyy.mybatis.base.BaseMapper2;
import com.ddy.dyy.web.uc.models.admin.UserQuery;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * .
 */
@Mapper
public interface UserMapper extends BaseMapper2<UserEntity> {


    @Select("select * from t_user where deleted = 0 "
            + "and app_id = #{appId} "
            + "and username = #{username}")
    UserEntity getByUsername(@Param("appId") long appId,
                             @Param("username") String username);

    @Select("select * from t_user where uid = #{uid}")
    UserEntity getByUid(@Param("uid") String uid);


    @Select("select count(*) from t_user where sid = #{sid}")
    int countBySid(@Param("sid") String sid);

    List<UserEntity> selectUserList(@Param("appId") Long appId, @Param("form") UserQuery form);

    int deleteUserByIds(@Param("ids") List<Long> ids);
}
