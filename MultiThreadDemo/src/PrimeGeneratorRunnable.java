import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PrimeGeneratorRunnable implements Runnable {

    private volatile boolean cancelled;
    private List<BigInteger> primeList = new ArrayList<>();


    @Override
    public void run() {
        BigInteger prime = BigInteger.ONE;
        while (!cancelled){
            prime = prime.nextProbablePrime();
            synchronized (this){
                primeList.add(prime);
            }
            if (primeList.size() == 10){
                throw  new RuntimeException("已经超过十个数le！");
            }
        }
    }

    public void cancel(){
        cancelled = true;
    }

    public List<? extends BigInteger> getList(){
        return new ArrayList<>(primeList);
    }

}
