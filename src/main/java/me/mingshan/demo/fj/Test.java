package me.mingshan.demo.fj;

import org.openjdk.jmh.annotations.Benchmark;

import java.util.stream.LongStream;

/**
 * @Author: mingshan
 * @Date: Created in 18:52 2018/10/14
 */
public class Test {
    private static long[] numbers = LongStream.rangeClosed(1L, 100_000_000L).toArray();

    @Benchmark
    public void test1() {
        // 1 直接for循环
        Calculator calculator = new ForLoopCalculatorImpl();
        long currentTime1 = System.currentTimeMillis();
        long result1 = calculator.sum(numbers);
    }

    @Benchmark
    public void test2() {
        // 2 利用线程池
        Calculator calculator2 = new ExecutorServiceCalculatorImpl();
        long currentTime2 = System.currentTimeMillis();
        long result2 = calculator2.sum(numbers);
    }

    @Benchmark
    public void test3() {
        // 3 fork/join
        Calculator calculator3 = new ForkJoinCalculatorImpl();
        long currentTime3 = System.currentTimeMillis();
        long result3 = calculator3.sum(numbers);
    }

//    public static void main(String[] args) {
//        long[] numbers = LongStream.rangeClosed(1L, 100_000_000L).toArray();
//
//        // 1 直接for循环
//        Calculator calculator = new ForLoopCalculatorImpl();
//        long currentTime1 = System.currentTimeMillis();
//        long result1 = calculator.sum(numbers);
//        long executedTime = System.currentTimeMillis() - currentTime1;
//        System.out.println("直接循环计算结果：" + result1 + ", 耗时：" + executedTime);
//
//        // 2 利用线程池
//        Calculator calculator2 = new ExecutorServiceCalculatorImpl();
//        long currentTime2 = System.currentTimeMillis();
//        long result2 = calculator2.sum(numbers);
//        long executedTime2 = System.currentTimeMillis() - currentTime2;
//        System.out.println("线程池计算结果：" + result2 + ", 耗时：" + executedTime2);
//
//        // 3 fork/join
//        Calculator calculator3 = new ForkJoinCalculatorImpl();
//        long currentTime3 = System.currentTimeMillis();
//        long result3 = calculator3.sum(numbers);
//        long executedTime3 = System.currentTimeMillis() - currentTime3;
//        System.out.println("Fork/Join计算结果：" + result3 + ", 耗时：" + executedTime3);
//    }
}
