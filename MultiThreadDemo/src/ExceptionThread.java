import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExceptionThread implements Runnable{
    @Override
    public void run() {
        throw new RuntimeException();

    }

    public static void main(String[] args) {
        final ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ExceptionThread());//在这里进行捕获是没有用的
    }

}
