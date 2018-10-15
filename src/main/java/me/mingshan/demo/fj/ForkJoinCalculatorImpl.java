package me.mingshan.demo.fj;

import java.util.concurrent.ForkJoinPool;

/**
 * @Author: mingshan
 * @Date: Created in 19:42 2018/10/15
 */
public class ForkJoinCalculatorImpl implements Calculator {
    private ForkJoinPool pool;

    @Override
    public long sum(long[] source) {
        pool = new ForkJoinPool();
        SumTask task = new SumTask(source, 0, source.length - 1);
        return pool.invoke(task);
    }
}
