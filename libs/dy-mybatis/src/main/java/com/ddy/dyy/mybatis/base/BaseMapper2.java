package com.ddy.dyy.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

/**
 * .
 *
 * @param <T> .
 */
public interface BaseMapper2<T> extends BaseMapper<T> {

    /**
     * .
     *
     * @return .
     */
    default QueryWrapper<T> wrapper() {
        return Wrappers.query();
    }

    /**
     * .
     *
     * @return .
     */
    default LambdaQueryWrapper<T> lambdaWrapper() {
        return Wrappers.lambdaQuery();
    }

}
