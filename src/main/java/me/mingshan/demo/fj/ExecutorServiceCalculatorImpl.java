package me.mingshan.demo.fj;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: mingshan
 * @Date: Created in 21:39 2018/10/15
 */
public class ExecutorServiceCalculatorImpl implements Calculator {
    private static final int parallism = Runtime.getRuntime().availableProcessors();
    private ExecutorService pool;

    public ExecutorServiceCalculatorImpl() {
        int corePoolSize = Math.max(2, Math.min(parallism - 1, 4));
        int maximumPoolSize = parallism * 2 + 1;
        int keepAliveTime = 30;
        //System.out.println(String.format("corePoolSize = %s, maximumPoolSize = %s", corePoolSize, maximumPoolSize));
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
        // 线程的创建工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "AdvacnedAsyncTask #" + mCount.getAndIncrement());
            }
        };

        // 线程池任务满载后采取的任务拒绝策略
        RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.DiscardOldestPolicy();

        pool = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory,
                rejectHandler);
    }

    @Override
    public long sum(long[] numbers) {
        List<Future<Long>> results = new ArrayList<>();

        // 把任务分解为 n 份，交给 n 个线程处理，
        // 此时由于int类型丢失精度
        int part = numbers.length / parallism;
        for (int i = 0; i < parallism; i++) {
            // 进行任务分配
            int from = i * part;
            // 最后一份任务可能不均匀，直接分配给最后一个线程
            int to = (i == parallism - 1) ? numbers.length - 1 : (i + 1) * part - 1;
            // 提交计算任务
            results.add(pool.submit(new SumTask(numbers, from, to)));
        }

        // 把每个线程的结果相加，得到最终结果
        long total = 0L;
        for (Future<Long> f : results) {
            try {
                total += f.get();
            } catch (Exception ignore) {}
        }
        pool.shutdown();
        return total;
    }

    private static class SumTask implements Callable<Long> {
        private long[] numbers;
        private int from;
        private int to;

        public SumTask(long[] numbers, int from, int to) {
            this.numbers = numbers;
            this.from = from;
            this.to = to;
        }

        @Override
        public Long call() throws Exception {
            long total = 0;
            for (int i = from; i <= to; i++) {
                total += numbers[i];
            }
            return total;
        }
    }
}
