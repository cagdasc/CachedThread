package com.cacaosd.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cacaosd.cachedthread.asyncmethod.annotation.AsyncMethod;
import com.cacaosd.cachedthread.asyncmethod.processor.AsyncMethodCaller;
import com.cacaosd.cachedthread.handler.TaskCallback;
import com.cacaosd.cachedthread.model.output.BaseRestOutput;
import com.cacaosd.sample.base.BaseThreadActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends BaseThreadActivity {

    private AsyncMethodCaller asyncMethodCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asyncMethodCaller  = AsyncMethodCaller.of(this, getCachedThreadPool()
                .getExecutorService(this)).init();

        final TextView firstTaskResult = findViewById(R.id.taskResultTextView);
        final TextView secondTaskResult = findViewById(R.id.secondTaskResultTextView);

        findViewById(R.id.startTaskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstTaskResult.setText("Task1 Started");
                CallableTask task = new CallableTask("task1");
                task.setTaskListener(new TaskListener() {
                    @Override
                    public void onTaskFinish(Object obj) {
                        System.out.println((String) obj);
                        firstTaskResult.setText((String) obj);
                    }
                });
                getCachedThreadPool().executeTask(task, MainActivity.this);
            }
        });

        findViewById(R.id.startSecondTaskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondTaskResult.setText("Task2 Started");
                CallableTask task = new CallableTask("task2");
                task.setTaskListener(new TaskListener() {
                    @Override
                    public void onTaskFinish(Object obj) {
                        System.out.println((String) obj);
                        secondTaskResult.setText((String) obj);
                    }
                });
                getCachedThreadPool().executeTask(task, MainActivity.this);
            }
        });

        final TextView methodCallResult = findViewById(R.id.methodCallResultTextView);
        final TextView paramMethodCallResult = findViewById(R.id.paramMethodCallResultTextView);

        findViewById(R.id.startMethodCallButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodCallResult.setText("Non param method called");
                asyncMethodCaller.callMethod(MainActivity.this, 1001).execute(new TaskCallback<BaseRestOutput>() {
                    @Override
                    public void onResult(BaseRestOutput baseRestOutput) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                methodCallResult.setText("Non param method finish");
                            }
                        });

                    }
                }); // Async call
            }
        });

        findViewById(R.id.startParamMethodCallButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramMethodCallResult.setText("Parametric method called");
                try {
                    List<Object> params = new ArrayList<>();
                    params.add("Param1");
                    asyncMethodCaller.callMethod(MainActivity.this, 1002, params).get(); // Wait until finish
                    paramMethodCallResult.setText("Parametric method finish");
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @AsyncMethod(id = 1001)
    public void nonParametricMethod() {
        for (int i = 0; i < 10; i++) {
            Log.d("TAG", "do some work");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @AsyncMethod(id = 1002)
    public void parametricMethod(String param1) {
        for (int i = 0; i < 10; i++) {
            Log.d("TAG", param1 + " do some work");
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        asyncMethodCaller.destroy();
    }
}
