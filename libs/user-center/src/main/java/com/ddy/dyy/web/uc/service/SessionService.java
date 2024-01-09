package com.ddy.dyy.web.uc.service;

import com.ddy.dyy.mybatis.base.BaseServiceImpl;
import com.ddy.dyy.web.uc.mapper.SessionMapper;
import com.ddy.dyy.web.uc.models.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SessionService extends BaseServiceImpl<SessionMapper, SessionEntity> {


    @Autowired
    private SessionMapper dao;

}
