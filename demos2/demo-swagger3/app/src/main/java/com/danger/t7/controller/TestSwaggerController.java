package com.danger.t7.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * swagger 用户测试方法
 */
@Tag(name = "用户信息管理", description = "<span style='color:red'>用户信息管理desc</span>")
@RestController
@RequestMapping("/admin/user")
public class TestSwaggerController {
    private final static Map<Integer, UserEntity> users = new LinkedHashMap<Integer, UserEntity>();

    {
        users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
        users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
    }

    @Operation(summary = "获取用户列表", description = "获取用户列表-description")
    @GetMapping("/list")
    public List<UserEntity> userList() {
        List<UserEntity> userList = new ArrayList<UserEntity>(users.values());
        return userList;
    }

    @Operation(summary = "获取用户详细")
    @Parameter(name = "用户ID", description = "用户ID desc", example = "1", required = true)
    @GetMapping("/{userId}")
    public UserEntity getUser(@PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            return users.get(userId);
        } else {
            return null;
        }
    }

    @Operation(summary = "获取用户详细2", description = "接口描述2", parameters = {
            @Parameter(schema = @Schema(implementation = UserRequest.class))
    })
    @GetMapping("/user2")
    public UserEntity2 getUser2(HttpServletRequest request, HttpServletResponse response,
                                String xxx,
                                @Parameter(description = "用户名222") String yyy,
                                UserRequest form) {
        return null;
    }

    @Operation(summary = "获取用户详细3", description = "接口描述3")
    @GetMapping("/user3")
    public UserEntity2 getUser3(@Validated @ModelAttribute UserRequest form) {

        return null;
    }

//    @Operation(summary = "忽略这个接口")
//    @PostMapping("xxx")
//    public String getxxxx(@RequestBody UserRequest form){
//        return "";
//    }

    @Operation(summary = "新增用户")
    @Parameter(name = "新增用户信息")
    @PostMapping("/save")
    public UserEntity save(@RequestBody UserEntity user) {
        return users.put(user.getUserId(), user);
    }

    @Operation(summary = "更新用户")
    @Parameter(name = "更新用户信息")
    @PutMapping("/update")
    public UserEntity update(UserEntity user) {
        users.remove(user.getUserId());
        return users.put(user.getUserId(), user);
    }

    @Operation(summary = "删除用户信息")
    @Parameter(name = "用户ID", required = true)
    @DeleteMapping("/{userId}")
    public String delete(@PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            users.remove(userId);
            return "ok";
        } else {
            return "用户不存在";
        }
    }
}

