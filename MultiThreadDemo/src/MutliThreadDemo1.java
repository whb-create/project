import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MutliThreadDemo1 {


    public static void main(String[] args) {
        System.out.println("开始写第一个多线程的demo！");

        ExecutorService executorService  =Executors.newSingleThreadExecutor();
       Future future=  executorService.submit(new Runnable() {
            @Override
            public void run() {

            }
        });


       ExecutorService executorService1 =Executors.newSingleThreadExecutor();
       executorService1.shutdownNow();


    }


}
