package com.ddy.dyy.web.uc.web;

import java.util.ArrayList;
import java.util.List;

import com.ddy.dyy.web.models.Response;
import com.ddy.dyy.web.uc.UserCenterRestController;
import com.ddy.dyy.web.uc.models.DictVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@UserCenterRestController("/admin/dict")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminDictController {


    @GetMapping("/get_by_type")
    public Response<List<DictVo>> getByType(String dictType) {
        List<DictVo> list = new ArrayList<>();
        DictVo d = null;
        if ("sys_common_status".equals(dictType)) {
            d = new DictVo();
            d.setType(dictType);
            d.setLabel("启用");
            d.setDictValue("1");
            list.add(d);
            d = new DictVo();
            d.setType(dictType);
            d.setLabel("禁用");
            d.setDictValue("0");
            list.add(d);
        } else if ("sys_user_sex".equals(dictType)) {
            d = new DictVo();
            d.setType(dictType);
            d.setLabel("男");
            d.setDictValue("0");
            list.add(d);
            d = new DictVo();
            d.setType(dictType);
            d.setLabel("女");
            d.setDictValue("1");
            list.add(d);
            d = new DictVo();
            d.setType(dictType);
            d.setLabel("保密");
            d.setDictValue("2");
            list.add(d);
        }
        return Response.ok(list);
    }
}
