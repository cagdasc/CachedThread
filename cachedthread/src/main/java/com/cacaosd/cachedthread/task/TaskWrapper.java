package com.cacaosd.cachedthread.task;

import com.cacaosd.cachedthread.handler.TaskCallback;
import com.cacaosd.cachedthread.model.output.BaseRestOutput;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class TaskWrapper<T> {

    private ExecutorService executorService;
    private CallablePoolTask<BaseRestOutput> callablePoolTask;

    public TaskWrapper(ExecutorService executorService, CallablePoolTask<BaseRestOutput> callablePoolTask) {
        this.executorService = executorService;
        this.callablePoolTask = callablePoolTask;
    }

    public void execute() {
        execute(null);
    }

    public void execute(TaskCallback<BaseRestOutput> taskCallback) {
        callablePoolTask.setCallback(taskCallback);
        executorService.submit(callablePoolTask);
    }

    public Object get() throws ExecutionException, InterruptedException {
        return executorService.submit(callablePoolTask).get();
    }

}
