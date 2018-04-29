package com.cacaosd.cachedthread.handler;

import com.cacaosd.cachedthread.model.output.BaseRestOutput;

public interface TaskCallback<T extends BaseRestOutput> {

    void onResult(T t);
}
