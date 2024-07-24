import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;

import java.util.concurrent.TimeUnit;

public class SImpleDaemons implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {

                TimeUnit.MILLISECONDS.sleep(100);

                System.out.println(Thread.currentThread()+" "+this);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            final Thread thread = new Thread(new SImpleDaemons());
            thread.setDaemon(true);
            thread.start();
        }

        TimeUnit.MILLISECONDS.sleep(100);

    }

}
