package me.mingshan.demo.fj;

/**
 *
 * @Author: mingshan
 * @Date: Created in 21:37 2018/10/15
 */

public class ForLoopCalculatorImpl implements Calculator {

    @Override
    public long sum(long[] numbers) {
        long result = 0L;
        for (int i = 0; i < numbers.length; i++) {
            result += numbers[i];
        }
        return result;
    }
}