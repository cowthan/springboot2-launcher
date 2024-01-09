package com.ddy.demo.y;

import com.ddy.dyy.web.models.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @RequestMapping("/api/tools/longTask")
    public Response<Object> home() {
        return Response.ok("ok");
    }
    @RequestMapping("/api/tools/bytesLookup")
    public Response<Object> bytesLookup() {
        return Response.ok("ok");
    }
    @RequestMapping("/api/tools/dataConvert")
    public Response<Object> dataConvert() {
        return Response.ok("ok");
    }
}
