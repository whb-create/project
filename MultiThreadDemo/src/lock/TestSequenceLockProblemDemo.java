package lock;

import java.util.Random;

public class TestSequenceLockProblemDemo {
    private static final Object tieLock = new Object();

    private int originMoney, destinationMoney;

    public void transferMoney(final int fromCount, final int toCount, final long amount) {
        class Helper {
            public void transfer() {
//                if (fromCount < amount) {
//                    throw new IllegalStateException("需要有初始资金！");
//                } else {
                    originMoney = (int) (fromCount - amount);
                    destinationMoney = (int) (toCount + amount);
//                }
            }
        }

        int formHash = System.identityHashCode(fromCount);
        int toHash = System.identityHashCode(toCount);

        if (formHash > toHash) {
            synchronized ((Object) fromCount) {
                synchronized ((Object) toCount) {
                    new Helper().transfer();
                }
            }
        } else if (toHash > formHash) {
            synchronized ((Object) toCount) {
                synchronized ((Object) formHash) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized ((Object) fromCount) {
                    synchronized ((Object) toCount) {
                        new Helper().transfer();
                    }
                }
            }
        }

    }


    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;

    public static void main(String[] args) {

        final TestSequenceLockProblemDemo testSequenceLockProblemDemo = new TestSequenceLockProblemDemo();

        final Random rnd = new Random();
        final int[] accounts = new int[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = rnd.nextInt();
        }

        class TransferThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int formCount = rnd.nextInt(NUM_ACCOUNTS);
                    int toCount = rnd.nextInt(NUM_ACCOUNTS);

                    long dollarAmount = rnd.nextLong();

                    testSequenceLockProblemDemo.transferMoney(formCount, toCount, dollarAmount);
                }


            }
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }

    }

}
