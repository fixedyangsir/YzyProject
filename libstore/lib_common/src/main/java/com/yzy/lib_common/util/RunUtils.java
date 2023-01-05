package com.yzy.lib_common.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RunUtils {

    private static Executor mThreadPool = Executors.newCachedThreadPool();
    private static Handler mHandelr = new Handler(Looper.getMainLooper());

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Handler getMainHandler() {
        return mHandelr;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 在ui线程执行
     *
     * @param runnable
     */
    public static void runMainThread(Runnable runnable) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            runOnUIThread(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * 在线程池中运行一个任务
     *
     * @param runnable
     */
    public static void runThread(Runnable runnable) {
        mThreadPool.execute(runnable);
    }

    /**
     * post 在主线程运行一个任务
     *
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        mHandelr.post(runnable);
    }

    /**
     * 延时在主线程运行一个任务
     *
     * @param runnable
     * @param delayMillis
     */
    public static void runOnUIThreadDelayed(Runnable runnable, long delayMillis) {
        mHandelr.postDelayed(runnable, delayMillis);
    }

    /**
     * 移除一个任务
     *
     * @param runnable
     */
    public static void removeRunnable(Runnable runnable) {
        mHandelr.removeCallbacks(runnable);
    }
    public static void clear() {
        mHandelr.removeCallbacksAndMessages(null);
    }

}