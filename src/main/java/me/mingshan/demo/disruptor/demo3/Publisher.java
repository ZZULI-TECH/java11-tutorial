package me.mingshan.demo.disruptor.demo3;

import com.lmax.disruptor.dsl.Disruptor;
import me.mingshan.demo.disruptor.Element;


public class Publisher implements Runnable {
    Disruptor<Element> disruptor;
    private static int LOOP=10000000;//模拟一千万次交易的发生

    public Publisher(Disruptor<Element> disruptor) {
        this.disruptor = disruptor;
    }

    @Override
    public void run() {
        TradeTransactionEventTranslator tradeTransloator = new TradeTransactionEventTranslator();
        for(int i=0;i<LOOP;i++){
            disruptor.publishEvent(tradeTransloator);
        }
    }
}
