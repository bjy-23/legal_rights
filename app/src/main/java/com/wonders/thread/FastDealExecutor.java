package com.wonders.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bjy on 2017/6/15.
 */

public class FastDealExecutor {
    private static ExecutorService executorService;

    //for single instance
    private FastDealExecutor(){

    }

    //for single instance
    private static ExecutorService build(){
        if (executorService == null){
            synchronized (FastDealExecutor.class){
                if (executorService == null){
                    synchronized (FastDealExecutor.class){
                        executorService = Executors.newCachedThreadPool();
                    }
                }
            }
        }

        return executorService;
    }

    public static void run(Runnable r){
        build().execute(r);
    }
}
