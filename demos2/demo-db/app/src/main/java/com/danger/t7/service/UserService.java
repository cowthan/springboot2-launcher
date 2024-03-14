package com.danger.t7.service;

import java.util.List;

import org.danger.utils.mybatis.BaseServiceImpl;
import com.danger.t7.entity.UserEntity;
import com.danger.t7.mapper.UserMapper;
import org.danger.utils.mybatis.PageCriteria;
import org.danger.utils.mybatis.PageData;
import org.danger.utils.mybatis.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MessageService
 */
@Service
public class UserService extends BaseServiceImpl<UserMapper, UserEntity> {
    @Autowired
    private UserMapper mapper;


    /**
     * findById
     *
     * @param id id
     * @return result
     */
    public UserEntity findById(Long id) {
        return mapper.findById(id);
    }


    /**
     * updateStatus
     *
     * @param id     id
     * @param status status
     * @return result
     */
    public int updateStatus(long id, int status) {
        return mapper.updateStatus(id, status);
    }


    /**
     * 获取消息列表，默认按id倒序
     *
     * @param pageCriteria .
     * @return .
     */
    public PageData<UserEntity> findUsers(PageCriteria pageCriteria) {
        PageUtils.startPage(pageCriteria);
        List<UserEntity> messageEntities = list(lambdaWrapper().orderByDesc(UserEntity::getId));
        PageData<UserEntity> pageData = PageUtils.buildTo(messageEntities);
        return pageData;
    }


}

