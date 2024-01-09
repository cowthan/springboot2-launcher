package com.ddy.dyy.web.uc.service;

import com.ddy.dyy.mybatis.base.BaseServiceImpl;
import com.ddy.dyy.web.uc.mapper.AdminMenuMapper;
import com.ddy.dyy.web.uc.models.entity.AdminMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class AdminMenuService extends BaseServiceImpl<AdminMenuMapper, AdminMenuEntity> {

    @Autowired
    private AdminMenuMapper dao;

}
