package com.ddy.dyy.mybatis;

import java.util.List;

import com.ddy.dyy.mybatis.mapper.SchemeMapper;
import com.ddy.dyy.web.models.biz.AssocArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 */
@Service
public class SchemeService {

    @Autowired
    SchemeMapper dao;

    public List<AssocArray> select(String sql, AssocArray map){
        return dao.select(sql, map);
    }


    public int update(String sql, AssocArray map){
        return dao.update(sql, map);
    }

    public int count(String sql, AssocArray map){
        List<AssocArray> list = dao.select("select count(*) count " + sql, map);
        AssocArray row = list.get(0);
        try {
            return Integer.parseInt(row.get("count")+"");
        }catch (Exception e){
            return 0;
        }
    }
}
