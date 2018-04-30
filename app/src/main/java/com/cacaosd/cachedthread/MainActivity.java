package com.cacaosd.cachedthread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cacaosd.cachedthread.base.BaseThreadActivity;

public class MainActivity extends BaseThreadActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
