package com.fitechsource.test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Should be improved to reduce calculation time.
 * <p>
 * Change it or create new one. (max threads count is com.fitechsource.test.TestConsts#MAX_THREADS)
 */
public class Test {
    public static void main(String[] args) throws TestException, InterruptedException {
        final Set<Double> res = Collections.newSetFromMap(new ConcurrentHashMap<Double, Boolean>()); //thread-safe set
        final AtomicInteger currNumber = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < TestConsts.MAX_THREADS; i++) {
            CalcTask task = new CalcTask(currNumber, res);
            Thread worker = new Thread(task);

            threads.add(worker);
            worker.start();
        }
        threads.get(0).interrupt();
        for (Thread worker : threads) {
            worker.join();
        }

        System.out.println(System.currentTimeMillis() - start);
        System.out.println(res);
    }

    private static class CalcTask implements Runnable {
        private final AtomicInteger currNumber;
        private final Set<Double> res;

        private CalcTask(AtomicInteger currNumber, Set<Double> res) {
            this.currNumber = currNumber;
            this.res = res;
        }

        @Override
        public void run() {
            while (currNumber.get() < TestConsts.N) {
                int number = currNumber.getAndIncrement();
                if (number < TestConsts.N) {
                    try {
                        res.addAll(TestCalc.calculate(number));
                    } catch (TestException e) {
                        System.out.println("Exception caught. Queue will be cleared.");
                        currNumber.set(TestConsts.N);
                        break;
                    }
                }
            }
        }
    }
}
