package com.ddy.demo.x;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ddy.demo.x.models.entity.AnythingEntity;
import com.ddy.demo.x.service.AnythingService;
import com.ddy.dyy.web.models.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class ControlPlaneController {
    private final AnythingService anythingService;

    @GetMapping("/api/testInsert")
    public Response<List<AnythingEntity>> testInsert() {
        List<AnythingEntity> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AnythingEntity row = new AnythingEntity();
            row.setCode(UUID.randomUUID().toString());
            row.setType(UUID.randomUUID().toString());
            row.setKeyword(UUID.randomUUID().toString());
            row.setContent(UUID.randomUUID().toString());
            list.add(row);
            anythingService.save(row);
        }
        // 如果未开启事务，则退化为单条插入，性能很差，如saveBatch在service内被内部调用
//        anythingService.saveOrUpdateBatch(list, 100);
//        anythingService.saveBatch(list);
        return Response.ok(anythingService.list());
    }
}
