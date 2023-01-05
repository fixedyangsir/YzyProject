package com.yzy.lib_common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolUtils {

    private static ThreadPoolUtils mThreadPoolUtils = new ThreadPoolUtils();
    ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolUtils() {
        mThreadPoolExecutor = new ThreadPoolExecutor(    //
                10, 20, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(r.toString() + " is discard! ");
                    }
                });
    }

    public static ThreadPoolUtils getInstance() {
        return mThreadPoolUtils;

    }


    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }

}
