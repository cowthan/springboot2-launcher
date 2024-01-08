

基于myabtis的数据库版本管理工具

# 基础知识

## 1、背景
微服务十几二十个，各有各的库，上新版本时总是忘记修改数据库，修改起来繁琐，就想找个自动管理数据库版本的库，找了找，
- flyway据说太重，没考虑，
- 查了一下有个mybatis-mate提供了DDL管理工具，但不开源，并且得授权，否则jar包启动不了，

也没啥办法了， 就参考mybatis-mate，加了个班写了这个库，暂时只支持mysql



## 2 mybatis工具类：SqlRunner

SqlRunner是MyBatis提供了一个用于操作数据库的工具类，对JDBC做了很好的封装。结合工具类SQL，能够很方便地通过Java代码执行SQL语句并检索SQL执行结果

```java
String sql = new SQL()
    .SELECT("*")
	.FROM("user_info")
	.WHERE("id = #{id}", "name = #{name}")
	.toString();
Connection connection = DriverManager.getConnection("数据库连接");
SqlRunner sqlRunner = new SqlRunner(connection);
Map<String, Object> result = sqlRunner.selectOne(sql, 1, "Jason");

提供的其他执行sql语句的方法：
SqlRunner#insert(String sql, Object… args)：执行一条INSERT语句，插入一条记录。
SqlRunner#update(String sql, Object… args)：执行更新SQL语句。
SqlRunner#delete(String sql, Object… args)：执行删除SQL语句。
SqlRunner#run(String sql)：执行任意一条SQL语句，最好为DDL语句。

```



## 3 mybatis工具类：ScriptRunner



SqlRunner是MyBatis提供了一个用于执行sql脚本的工具类，对JDBC做了很好的封装



```java
try{
    Connection connection = DriverManager.getConnection("数据库连接");
    ScriptRunner scriptRunner = new ScriptRunner(connection);
    scriptRunner.runScript(Resources.getResourceAsReader("mysql.sql"));
} catch(Exception e){
    // 异常
}
```

## 4 db_version表

使用本库会自动在数据库中创建db_version表，用于存储已经执行过的脚本，所以已经执行过的脚本就不能再改了


## 5 单脚本的事务支持

执行一个脚本，如多个alter语句，无法保证事务，需要开发者自己注意


## 6 版本回滚

不支持，不知道怎么弄

