import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Accessor implements Runnable {

    private final int id;

    public Accessor(int id) {
        this.id = id;

    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            ThreadLocalCase.increment();
            System.out.println(this);

            Thread.yield();
        }

    }

    @Override
    public String toString() {
        return "#id=" + id + " : " + ThreadLocalCase.get();
    }
}

public class ThreadLocalCase {

    private static final ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        private final Random random = new Random(47);

        @Override
        protected synchronized Integer initialValue() {
            return random.nextInt(10000);
        }
    };

    public static void increment() {
        value.set(value.get() + 1);
    }

    public static int get() {
        return value.get();
    }


    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            executorService.execute(new Accessor(i));
        }
        TimeUnit.SECONDS.sleep(1);

        executorService.shutdownNow();

    }

}
