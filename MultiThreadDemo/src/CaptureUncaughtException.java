import java.util.concurrent.*;

class ExceptionThread2 implements Runnable {
    @Override
    public void run() {
        final Thread thread = Thread.currentThread();
        System.out.println("run by = " + thread.getName());
        System.out.println("eh = " + thread.getUncaughtExceptionHandler());

        throw new RuntimeException();


    }
}

class MyUnCaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("t = " + t + ", 捕获了 e = " + e);
    }
}


class HandlerThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        System.out.println("由r = " + r + " 创建新线程");

        final Thread thread = new Thread(r);

        System.out.println("创建好了新线程 = " + thread);
        thread.setUncaughtExceptionHandler(new MyUnCaughtExceptionHandler());

        System.out.println("eh = " + thread.getUncaughtExceptionHandler());

        return thread;
    }

    @Override
    public String toString() {
        return "HandlerThreadFactory";
    }
}

class MyThredaPoolExecutor extends ThreadPoolExecutor{

    public MyThredaPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }
}

public class CaptureUncaughtException {
    public static void main(String[] args) {
        final ExecutorService exec = Executors.newCachedThreadPool(new HandlerThreadFactory());
        exec.execute(new ExceptionThread2());
        exec.shutdown();
    }
}
