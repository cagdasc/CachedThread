# CachedThread
Class based **ExecutorServices** implementation. You can implement to Activity or your class.

### Usage
1- You need to implement **PoolHandler** interface and create **CachedThreadPool** instance.
```java
public abstract class BaseThreadActivity extends AppCompatActivity implements PoolHandler {

    private CachedThreadPool mCachedThreadPool = CachedThreadPool.getInstance();
    ...
}
```
2- Adjust your thread configurations.
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
3- And open pool and close after your work was finished.
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

License
-------

      Copyright 2016 Cagdas Caglak

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.