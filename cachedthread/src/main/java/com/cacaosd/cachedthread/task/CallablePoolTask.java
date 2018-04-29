package com.cacaosd.cachedthread.task;

import android.os.Handler;
import android.os.Looper;

import com.cacaosd.cachedthread.handler.TaskCallback;
import com.cacaosd.cachedthread.model.output.BaseRestOutput;

import java.util.concurrent.Callable;

public abstract class CallablePoolTask<T extends BaseRestOutput> implements Callable<BaseRestOutput> {

    public enum Status {
        IDLE, START, PROCESS, FINISH
    }

    private TaskCallback<BaseRestOutput> mCallback;
    private Status mStatus;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public CallablePoolTask() {
        this.mStatus = Status.IDLE;
    }

    public void setCallback(TaskCallback<BaseRestOutput> mCallback) {
        this.mCallback = mCallback;
    }

    public void onStarted() {

    }

    public abstract T onProcess() throws Exception;

    public void onResult(BaseRestOutput result) {
        if (mCallback != null) {
            mCallback.onResult(result);
        }
    }

    @Override
    public BaseRestOutput call() throws Exception {
        this.mStatus = Status.START;
        onStarted();
        this.mStatus = Status.PROCESS;
        final BaseRestOutput baseRestOutput = onProcess();
        this.mStatus = Status.FINISH;
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                onResult(baseRestOutput);
            }
        });
        return baseRestOutput;
    }
}
