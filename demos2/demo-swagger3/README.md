
swagger

![image-20210901001159350](http://image.bighole.club/blog/android/nx0en.png)


FAQ  
https://springdoc.org/faq.html#how-can-i-configure-swagger-ui.


什么注解都不用加，也会生成swagger文档，大体也能看，但没啥大用
- 我们要swagger就是要页面+字段说明，但swagger3各个注解的description属性都不会显示在文档里，很叫人难受
- Operation注解是好使的，没啥
- Tag注解修饰类，但是会在文档里生成两个controller，也很奇怪
- 下面就是修饰参数的两种方式，其他任何方式，不管schema注解，parameter注解在别的什么地方都不好使
    - 修饰方法参数，就只能这么写，才会有description
    - 修饰UserRequest，就只能用ApiModelProperty才会有说明出现在文档里
    - 下面的UserRequest，也不会出现在实体列表里，也就是schema列表    

- 会出现在Schama列表里的实体：
    - @RequestBody的请求体
    - 接口方法返回的类，如下面的UserEntity2
    - 这时@Schema(description = "字段描述")注解是好使的
```

@Operation(summary = "获取用户详细2", description = "接口描述")
@GetMapping("/user2")
public UserEntity2 getUser2(HttpServletRequest request, HttpServletResponse response,
                            String xxx,
                            @Parameter(description = "用户名222") String yyy,
                            @Validated UserRequest form) {
    return null;
}


请求实体：注释掉的就是无效的

@Data
@Schema // 没用，不会出现在实体列表里
public class UserRequest {
//    @Schema(title = "用户ID", name = "userId用户id", description = "字段描述")
    @ApiModelProperty(name = "userId", value = "这是什么意思")
    private Integer userId;

//    @Parameter(name = "111", description = "2222", schema = @Schema(allowableValues = "这个字段是这个意思"))
    private String aaa;

}

响应实体：
@Schema
public class UserEntity2 {
    @Schema(description = "字段描述")
    private Integer userId;
}    
```

既然这么奇怪，这么不符合直觉，那就应该去官方找个demo看看了，实在不行得看源码了

问题：
1 方法参数如果是实体类，该类不会进入swagger的实体列表 -- 这不是问题，进入了也没用
2 方法参数如果是实体类，该类的@Schema注解不生效，还得使用@ApiModelProperty
3 Controller类上加@Tag，会在页面生成两栏，并且带中文说明的那一栏没法点