import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.concurrent.*;

public class CallerRunPolicyDemo {

    public class BoundedExecutor {
        private final Executor exec;
        private final Semaphore semaphore;

        public BoundedExecutor(Executor exec, int bound) {
            this.exec = exec;
            this.semaphore = new Semaphore(bound);
        }

        public void submitTask(final Runnable command) throws InterruptedException {

            semaphore.acquire();
            try {
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            command.run();
                        } finally {
                            semaphore.release();
                        }

                    }
                });
            } catch (RejectedExecutionException e) {
                semaphore.release();
            }

        }
    }


    public class MyThreadPoolExecutor extends ThreadPoolExecutor {

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        public void execute(Runnable command) {
            super.execute(command);
        }

        @Override
        protected void terminated() {
            super.terminated();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
        }
    }


    public static void main(String[] args) throws InterruptedException {

        ExecutorService exec = Executors.newFixedThreadPool(5, Executors.privilegedThreadFactory());

        Object sync = new Object();

        int code =System.identityHashCode(sync);

    }

}
