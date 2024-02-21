package com.gee.blg.test2;

import java.util.EmptyStackException;
import java.util.Stack;

import com.gee.blg.test.SpringBootJUnitApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 嵌套测试是 JUnit 5 的一个高级特性，它支持我们在编写单元测试类时，以内部类的方式组织一些有关联的测试逻辑
 * 官方提供的测试代码都是可以执行通过的，从这段测试代码中需要各位了解的几个关键特性：
 * - 单元测试类可以通过编写内部类，并标注 @Nested 注解，表明内部类也是一个单元测试类；
 * - 内部的单元测试类可以直接使用外部的成员属性，且可以利用外部定义的 @BeforeEach 、@BeforeAll 、@AfterEach 、@AfterAll 等前后置逻辑注解标注的方法；
 * - 外部的单元测试无法利用内部类定义的前后置逻辑注解。
 */
@SpringBootTest(classes = SpringBootJUnitApplication.class)
public class 嵌套测试 {

    Stack<Object> stack;

    @Test
    void isInstantiatedWithNew() {
        new Stack<>();
    }

    @Nested
    class WhenNew {

        @BeforeEach
        void createNewStack() {
            stack = new Stack<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            Assertions.assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        void throwsExceptionWhenPopped() {
            Assertions.assertThrows(EmptyStackException.class, () -> stack.pop());
        }

        @Nested
        @DisplayName("after pushing an element")
        class AfterPushing {

            String anElement = "an element";

            @BeforeEach
            void pushAnElement() {
                stack.push(anElement);
            }

            @Test
            @DisplayName("it is no longer empty")
            void isNotEmpty() {
                Assertions.assertFalse(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when popped and is empty")
            void returnElementWhenPopped() {
                Assertions.assertEquals(anElement, stack.pop());
                Assertions.assertTrue(stack.isEmpty());
            }
        }
    }
}
