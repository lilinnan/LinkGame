package com.lln.link.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/8 23:49
 */

public class GameThreadFactory implements ThreadFactory {
    private final String prefix;
    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    public GameThreadFactory(String prefix) {
        this.prefix = prefix + "-";
    }


    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + atomicInteger.getAndIncrement();
        return new Thread(r, name);
    }
}
