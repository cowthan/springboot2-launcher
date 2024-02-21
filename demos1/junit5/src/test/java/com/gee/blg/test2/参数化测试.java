package com.gee.blg.test2;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/*
参数化测试是 JUnit 5 中提高单元测试效率的重要手段，它通过给单元测试方法传入特定的参数，可以使得 JUnit 在执行单元测试时逐个参数来检验和测试，这样做的好处是更加规整和高效地执行单元测试。

参数化测试支持我们使用如下的方式赋予参数：
- 基本类型：8 种基本数据类型 + String + Class ；
- 枚举类型：自定义的枚举；
- CSV 文件：可传入一个 CSV 格式的表格文件，使用表格文件中的数据作为入参；
- 方法的数据返回：可以通过一个方法返回需要测试入参的数据（流的形式返回）。
 */
public class 参数化测试 {


    /*
    我们先看一下如何利用简单的基本类型完成参数化测试。在使用参数化测试时，标注的注解不再是 @Test ，取而代之的是 @ParameterizedTest ；另外还需要声明指定需要传入的数据，对于简单的基本类型而言，使用 @ValueSource 注解即可指定
     */
    @ParameterizedTest
    @ValueSource(strings = {"aa", "bb", "cc"})
    public void testSimpleParameterized(String value) throws Exception {
        System.out.println(value);
        Assertions.assertTrue(value.length() < 3);
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void testDataStreamParameterized(Integer value) throws Exception {
        System.out.println(value);
        Assertions.assertTrue(value < 10);
    }

    private static Stream<Integer> dataProvider() {
        return Stream.of(1, 2, 3, 4, 5);
    }
}
