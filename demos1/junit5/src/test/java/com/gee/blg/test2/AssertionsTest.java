package com.gee.blg.test2;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import com.gee.blg.test.SpringBootJUnitApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = SpringBootJUnitApplication.class)
public class AssertionsTest {
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 在 JUnit 4 中我们使用 Assert 类进行断言，而到了 JUnit 5 中使用的类是 Assertions ，类名变了，使用方式却大差不差
     */
    @Test
    public void testSimple() {
        System.out.println(restTemplate);
        // 最简单的断言，断言计算值与预期值是否相等
        int num = 3 + 5;
        Assertions.assertEquals(num, 8);

        double result = 10.0 / 3;
        // 断言计算值是否在浮点数的指定范围内上下浮动
        Assertions.assertEquals(result, 3, 0.5);
        // 如果浮动空间不够，则会断言失败
        // Assertions.assertEquals(result, 3, 0.2);
        // 传入message可以自定义错误提示信息
        Assertions.assertEquals(result, 3, 0.2, "计算数值偏差较大！");

        // 断言两个对象是否是同一个
        Object o1 = new Object();
        Object o2 = o1;
        Object o3 = new Object();
        Assertions.assertSame(o1, o2);
        Assertions.assertSame(o1, o3);

        // 断言两个数组的元素是否完全相同
        String[] arr1 = {"aa", "bb"};
        String[] arr2 = {"aa", "bb"};
        String[] arr3 = {"bb", "aa"};
        Assertions.assertArrayEquals(arr1, arr2);
        Assertions.assertArrayEquals(arr1, arr3);
    }

    /**
     * 合条件断言，实际上是要在一条断言中组合多个断言，要求这些断言同时、全部通过，则外部的组合断言才能通过。这种设计有点类似于父子断言
     */
    @Test
    public void testCombination() {
        Assertions.assertAll(
                () -> {
                    int num = 3 + 5;
                    Assertions.assertEquals(num, 8);
                },
                () -> {
                    String[] arr1 = {"aa", "bb"};
                    String[] arr2 = {"bb", "aa"};
                    Assertions.assertArrayEquals(arr1, arr2);
                }
        );
    }

    /**
     * 异常抛出的断言，指的是被测试的内容最终运行时必定会抛出一个异常，如果没有抛出异常则断言失败。换句话说，编写异常抛出断言时，我期望你这段代码一定会出现异常，有异常是对的，没有异常反而有问题
     */
    @Test
    public void testException() {
        Assertions.assertThrows(ArithmeticException.class, () -> {
            int i = 1 / 0;
        });
    }

    /**
     * 执行超时断言是，针对的是被测试代码的执行速度
     * - 指定了被测试逻辑需要在 500 ms 之内运行完毕，但是测试代码中有一个 1 秒的线程睡眠，则最终一定会因为执行超时而抛出异常
     */
    @Test
    public void testTimeout() {
        Assertions.assertTimeout(Duration.ofMillis(500), () -> {
            System.out.println("testTimeout run ......");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("testTimeout finished ......");
        });
    }

    /**
     * 强制失败，它的使用方式比较类似于最原始的抛出异常的方式
     */
    @Test
    public void testFail() {
        if (ZonedDateTime.now().getHour() > 12) {
            Assertions.fail();
        }
    }

    /**
     * 前置条件的检查机制，同样应用在断言的场景中，它指的是：如果一个单元测试的前置条件不满足，则当前的测试会被跳过，后续的测试不会执行。使用前置条件检查机制，可以避免一些无谓的测试逻辑执行，从而提高单元测试的执行效率。
     * - 先判断 num 的值是否小于 10 ，如果检查通过，则会执行后续的断言逻辑；而如果检查不通过，则这个单元测试会被跳过（ ignore ），后续的断言逻辑不会执行
     * @throws Exception
     */
    @Test
    public void testAssumptions() {
        int num = 3 + 5;
        Assumptions.assumeTrue(num < 8);

        Assertions.assertTrue(10 - num > 0);
    }
}
