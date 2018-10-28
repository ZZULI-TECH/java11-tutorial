package me.mingshan.demo.disruptor.demo2;

import com.lmax.disruptor.*;
import me.mingshan.demo.disruptor.Element;

import java.util.concurrent.*;

public class Test {
    public static final int BUFFER_SIZE = 1024;
    public static final int THREAD_NUMBERS = 4;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*
         * createSingleProducer创建一个单生产者的RingBuffer，
         * 第一个参数叫EventFactory，从名字上理解就是“事件工厂”，其实它的职责就是产生数据填充RingBuffer的区块。
         * 第二个参数是RingBuffer的大小，它必须是2的指数倍 目的是为了将求模运算转为&运算提高效率
         * 第三个参数是RingBuffer的生产都在没有可用区块的时候(可能是消费者（或者说是事件处理器） 太慢了)的等待策略
         */
        final RingBuffer<Element> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        }, BUFFER_SIZE,new YieldingWaitStrategy());

        //创建线程池
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMBERS);
        //创建SequenceBarrier
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();


        // 处理Event的handler
        EventHandler<Element> handler = new EventHandler<Element>(){
            @Override
            public void onEvent(Element element, long sequence, boolean endOfBatch)
            {
                System.out.println("Element: " + element.getValue());
            }
        };

        // 创建消息处理器
        BatchEventProcessor<Element> eventProcessor = new BatchEventProcessor<Element>(
                ringBuffer, sequenceBarrier, handler);

        ringBuffer.addGatingSequences(eventProcessor.getSequence());
        executors.submit(eventProcessor);

        long seq;
        for (int i = 0;i < 1000; i++) {
            seq = ringBuffer.next();//占个坑 --ringBuffer一个可用区块

            ringBuffer.get(seq).setValue(11);//给这个区块放入 数据  如果此处不理解，想想RingBuffer的结构图

            ringBuffer.publish(seq);//发布这个区块的数据使handler(consumer)可见
        }

        Thread.sleep(1000);//等上1秒，等消费都处理完成
        eventProcessor.halt();//通知事件(或者说消息)处理器 可以结束了（并不是马上结束!!!）
        executors.shutdown();//终止线程
    }
}
