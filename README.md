# CachedThread
Class based **ExecutorServices** implementation and Async method capsulation library. You can implement **CachedThred** to
Activity or your class and call your methods in thread not write some threaded codes.

### CachedThread Usage
1-You need to implement **PoolHandler** interface and create **CachedThreadPool** instance.
```java
public abstract class BaseThreadActivity extends AppCompatActivity implements PoolHandler {

    private CachedThreadPool mCachedThreadPool = CachedThreadPool.getInstance();
    ...
}
```
2-Adjust your thread configurations.
```java
        @Override
        public String getPoolName() {
            return this.getClass().getSimpleName(); // Uniqe pool name.
        }

        @Override
        public int getAwaitMillisTime() {
            return 300; // Await time before close pool.
        }

        @Override
        public ThreadConfiguration getThreadConfiguration() {
            // Your thread configuration.
            BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
            int numberOfCores = Runtime.getRuntime().availableProcessors();
            return ThreadConfiguration.newBuilder()
                    .setNumberOfCores(numberOfCores)
                    .setMaximumPoolSize(numberOfCores * 2)
                    .setKeepAliveTime(2)
                    .setKeepAliveTimeUnit(TimeUnit.SECONDS)
                    .setBlockingQueue(taskQueue)
                    .setThreadFactory(new BackgroundThreadFactory())
                    .setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy())
                    .build();
        }
```
3-And open pool and close after your work was finished.
```java
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mCachedThreadPool.openNewPool(this);

        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mCachedThreadPool.closePool(this);
        }

        public CachedThreadPool getCachedThreadPool() {
            return mCachedThreadPool;
        }
```
### AsyncMethod Usage
1-First you need to instante an AsyncMethod object.
```java
        public class MainActivity extends BaseThreadActivity {

            private AsyncMethodCaller asyncMethodCaller;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                asyncMethodCaller  = AsyncMethodCaller.of(this, getCachedThreadPool()
                        .getExecutorService(this)).init();
                ...
        }
```
2-Mark your method which run in thread.
```java
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
```
3-After you can call methods like this.
```java
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
        }); // Async call or call non-parametric execute()

        try {
            List<Object> params = new ArrayList<>();
            params.add("Param1");
            asyncMethodCaller.callMethod(MainActivity.this, 1002, params).get(); // Wait until finish
            paramMethodCallResult.setText("Parametric method finish");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
```
4-Do not forget clear the method cache for performance
```java
        @Override
        protected void onStop() {
            super.onStop();
            asyncMethodCaller.destroy();
        }
```
License
-------

      Copyright 2018 Cagdas Caglak

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.