package com.ddy.demo.x.service;

import java.util.UUID;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddy.demo.x.mapper.AnythingMapper;
import com.ddy.demo.x.models.entity.AnythingEntity;
import org.springframework.stereotype.Service;

@Service
public class AnythingService extends ServiceImpl<AnythingMapper, AnythingEntity> {

    public long saveAnything(String type, String keyword, String content) {
        String code = UUID.randomUUID().toString().replace("-", "");
        AnythingEntity row = new AnythingEntity();
        row.setCode(code);
        row.setType(type);
        row.setKeyword(keyword);
        row.setContent(content);
        save(row);
        return row.getId();
    }

}
