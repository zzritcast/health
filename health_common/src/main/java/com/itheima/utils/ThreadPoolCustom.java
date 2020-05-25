package com.itheima.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadPoolCustom
 * @Author HETAO
 * @Date 2020/4/23 21:52
 */
/*public class ThreadPoolCustom {
    public static ThreadPoolExecutor poolExecutor;

    static {
        int count = Runtime.getRuntime().availableProcessors();
        poolExecutor = new ThreadPoolExecutor(3, count, 3, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        ;
    }
}*/
