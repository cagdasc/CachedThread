package com.cacaosd.cachedthread.task;

import android.os.Handler;
import android.os.Looper;

import com.cacaosd.cachedthread.handler.TaskCallback;
import com.cacaosd.cachedthread.model.output.BaseRestOutput;

import java.util.concurrent.Callable;

public abstract class CallablePoolTask<T extends BaseRestOutput> implements Callable<BaseRestOutput> {

    public enum Status {
        START, PROCESS, FINISH
    }

    private TaskCallback<BaseRestOutput> mCallback;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public void setCallback(TaskCallback<BaseRestOutput> mCallback) {
        this.mCallback = mCallback;
    }

    public void onStarted() {

    }

    public void getStatus(Status status) {
    }

    public abstract T onProcess() throws Exception;

    public void onResult(BaseRestOutput result) {
        if (mCallback != null) {
            mCallback.onResult(result);
        }
    }

    @Override
    public BaseRestOutput call() throws Exception {
        getStatus(Status.START);
        onStarted();
        getStatus(Status.PROCESS);
        final BaseRestOutput baseRestOutput = onProcess();
        getStatus(Status.FINISH);
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                onResult(baseRestOutput);
            }
        });
        return baseRestOutput;
    }
}
