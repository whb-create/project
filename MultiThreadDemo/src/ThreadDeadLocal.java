import java.util.concurrent.*;

public class ThreadDeadLocal {

    ExecutorService exec = Executors.newFixedThreadPool(6);


    public class LoadFileTask implements Callable<String> {

        private final String fileName;

        public LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public String call() throws Exception {
            return "";
        }
    }

    public class RenderPage implements Callable<String> {

        @Override
        public String call() throws Exception {
            Future<String> header, footer;

            header = exec.submit(new LoadFileTask("head.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));

            String page = renderBody();

            return header.get() + page + footer.get();
        }

        public String renderBody() {
            return "";
        }

    }

    public void test() {
        //需要优先返回head和footer才能执行该方法，否则顺序就不对了，但是header和footer在等待当前线程释放才能执行
        exec.submit(new RenderPage());
    }

    public static void main(String[] args) {
        final ThreadDeadLocal threadDeadLocal = new ThreadDeadLocal();

        threadDeadLocal.test();

        Runtime runtime = Runtime.getRuntime();


    }

}
