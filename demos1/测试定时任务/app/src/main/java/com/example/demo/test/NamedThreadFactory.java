package com.example.demo.test;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * .
 */
public class NamedThreadFactory implements ThreadFactory {
    private String name;
    private AtomicInteger count;

    public NamedThreadFactory(String name) {
        this.name = name;
        count = new AtomicInteger();
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name + "-" + count.incrementAndGet());
    }
}
