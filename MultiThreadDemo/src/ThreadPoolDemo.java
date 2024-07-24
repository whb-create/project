import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {

    //ThreadPoolExecutor为一些executor提供了基本的实现，这些executor是由newcachedThreadPool，newFixedThreadPool和
    //newScheduledThreadExecutor返回的。

    public static void main(String[] args) {
        new ThreadPoolExecutor(5, 10, 1L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

        Executors.newCachedThreadPool();

    }
}
