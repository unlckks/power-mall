package com.mingyun.uitls;



import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: MingYun
 * @Date: 2023-04-07 18:47
 */

public class EsImportThreadPool {
    public static ThreadPoolExecutor esPool = new ThreadPoolExecutor(
            4,
            2 * Runtime.getRuntime().availableProcessors(),
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(500),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );
}
