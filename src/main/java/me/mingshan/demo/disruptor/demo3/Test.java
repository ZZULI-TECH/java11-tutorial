package me.mingshan.demo.disruptor.demo3;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import me.mingshan.demo.disruptor.Element;

import java.util.concurrent.ThreadFactory;

public class Test {
    public static void main(String[] args) {
        int bufferSize=1024;
        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory(){
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "simpleThread");
            }
        };

        // 处理Event的handler
        EventHandler<Element> handler = (element, sequence, endOfBatch) -> System.out.println("Element: " + element.getValue());


        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor<Element>(factory, bufferSize, threadFactory,
                ProducerType.SINGLE, strategy);

        EventHandlerGroup<Element> handlerGroup = disruptor.handleEventsWith(new TradeTransactionVasConsumer(),
                handler);
        handlerGroup.then((element, sequence, endOfBatch) -> System.out.println("zzz"));
        disruptor.start();//启动
    }
}
