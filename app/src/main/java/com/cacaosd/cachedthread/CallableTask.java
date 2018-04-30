package com.cacaosd.cachedthread;

import android.os.Handler;
import android.os.Looper;

import java.util.Random;
import java.util.concurrent.Callable;

public class CallableTask implements Callable<Object> {

    private Handler handler = new Handler(Looper.getMainLooper());
    private String name;
    private TaskListener taskListener;

    public CallableTask(String name) {
        this.name = name;
    }

    public void setTaskListener(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    @Override
    public Object call() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println("Task Name: " + name + " -> " + i);
            Thread.sleep(new Random(100).nextInt(250));
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (taskListener != null)
                    taskListener.onTaskFinish(name + " was finished");
            }
        });

        return name + " was finished";
    }
}
