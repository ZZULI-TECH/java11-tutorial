package me.mingshan.demo.fj;

import java.util.concurrent.RecursiveTask;

/**
 * @Author: mingshan
 * @Date: Created in 18:03 2018/10/14
 */
public class SumTask extends RecursiveTask<Long> {
    public static final int THRESHOLD = 500000;
    private long[] numbers;
    private int start;
    private int end;

    public SumTask(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {

        // 判断问题规模
        if ((end - start) <= THRESHOLD) {
            long result = 0L;
            for (int i = start; i <= end; i++) {
                result += numbers[i];
            }
            return result;
        }

        // 将任务分割为多个小任务
        int middle = (start + end) / 2;
        SumTask taskLeft = new SumTask(numbers, start, middle);
        SumTask taskRight = new SumTask(numbers, middle + 1, end);
        invokeAll(taskLeft, taskRight);
        long result = taskLeft.join() + taskRight.join();

        return result;
    }
}
