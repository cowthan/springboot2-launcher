package com.ddy.dyy.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * service实现类的基类，继承MyBatisPlus的能力
 *
 * @param <M> mapper
 * @param <T> Entity
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 便捷QueryWrapper获取，少写一行泛型代码
     *
     * @return .
     */
    public QueryWrapper<T> wrapper() {
        return Wrappers.query();
    }

    /**
     * 便捷LambdaQueryWrapper获取，少写一行泛型代码
     *
     * @return .
     */
    public LambdaQueryWrapper<T> lambdaWrapper() {
        return Wrappers.lambdaQuery();
    }

    /**
     * 便捷LambdaQueryWrapper获取，少写一行泛型代码
     *
     * @return .
     */
    public LambdaUpdateWrapper<T> lambdaUpdateWrapper() {
        return Wrappers.lambdaUpdate();
    }

}
