package me.mingshan.demo.disruptor.demo3;

import com.lmax.disruptor.EventHandler;
import me.mingshan.demo.disruptor.Element;

public class TradeTransactionVasConsumer implements EventHandler<Element> {

    @Override
    public void onEvent(Element event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(event.getValue());
    }
}
