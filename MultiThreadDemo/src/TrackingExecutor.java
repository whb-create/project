import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


class RunnableTask implements Runnable {

    private final static Random random = new Random(47);

    private final int id;

    public RunnableTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println(id + "-----任务开始:" + Thread.currentThread().getName());
        int val = 0;
        for (int i = 0; i < 100; i++) {
            try {
                val++;
                TimeUnit.MILLISECONDS.sleep(random.nextInt(60));//会导致假阳性的情况，识别出的被取消任务可能已经结束。
                //产生的原因是任务执行的最后一条指令，以及线程池记录任务结束之间，线程池可能发生关闭，如果任务是幂等的（指多次执行得到一样的结果），就不会有问题
                //就是还没开始记录，但是线程池关闭了，导致最后一条指令执行了，然后后i面也跟着记录，所以导致现象就是记录没错，但是执行了最后一条指令。

            } catch (InterruptedException e) {
                //任务在返回师必须要维持线程的中断状态
                //因为异常处理会清除中断状态，所以要进行中断状态恢复

                System.out.println(e.toString() + ", 线程号=" + id + ", val=" + val);
                Thread.currentThread().interrupt();
            }
        }

        if (val == 100) {
            System.out.println(id + "----任务结束---" + Thread.currentThread().getName() + " " + val);
        }
    }

    @Override
    public String toString() {
        return "RunnableTask{" +
                "id=" + id +
                '}';
    }
}


public class TrackingExecutor extends AbstractExecutorService {
    private final ExecutorService executorService;
    private final Set<Runnable> taskedCancelledAtShutdown = Collections.synchronizedSet(new HashSet<>());

    public TrackingExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        executorService.execute(() -> {
            try {
                command.run();
            } finally {
                System.out.println("当前线程的shutdown状态为 = " + isShutdown() + ", 当前线程的打断状态为=" + Thread.currentThread().isInterrupted()
                        + " 线程= " + command);
                if (isShutdown() && Thread.currentThread().isInterrupted()) {//对于传入的线程都进行检查
                    //如果当前线程被终端并且被shutdown则加入到收集队列当中，以方便统计当前正在执行并且尚未执行完成的线程。
                    taskedCancelledAtShutdown.add(command);
                }
            }
        });
    }

    public List<Runnable> getCancelledTask() {
        if (!executorService.isTerminated()) {
            throw new IllegalStateException();
        }

        return new ArrayList<>(taskedCancelledAtShutdown);
    }

    public static void main(String[] args) throws InterruptedException {
        final TrackingExecutor trackingExecutor = new TrackingExecutor(Executors.newCachedThreadPool());

        for (int i = 0; i < 10; i++) {
            final RunnableTask runnableTask = new RunnableTask(i);
            trackingExecutor.execute(runnableTask);
        }

        TimeUnit.SECONDS.sleep(3);

        trackingExecutor.shutdownNow();

        TimeUnit.SECONDS.sleep(1);//给一些时间去给未完成后的任务完成

        final List<Runnable> cancelled = trackingExecutor.getCancelledTask();

        System.out.println("已经开始但是还没有结束的任务 = " + cancelled);
    }


}
