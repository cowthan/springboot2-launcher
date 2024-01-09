package com.ddy.demo.x.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddy.demo.x.models.entity.AnythingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AnythingMapper extends BaseMapper<AnythingEntity> {
    @Select("select * from t_anything where id = #{id}")
    AnythingEntity findById(long id);
}
