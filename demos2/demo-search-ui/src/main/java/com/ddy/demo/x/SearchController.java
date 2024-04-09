package com.ddy.demo.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ddy.dyy.web.models.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class SearchController {

    @GetMapping("/search")
    public Response<List> search( String key) {
        List list = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            Map row = new HashMap<>();
            row.put("img", "http://doc.geesatcom.io/uploads/geesat-core-network/images/m_45d4c2ceff223d1a241a9c58bf3b8df0_r.png");
            row.put("price", "1112");
            row.put("name", "name-" + i);
            row.put("shop", "shop-" + i);
            list.add(row);
        }
        return Response.ok(list);
    }
}
