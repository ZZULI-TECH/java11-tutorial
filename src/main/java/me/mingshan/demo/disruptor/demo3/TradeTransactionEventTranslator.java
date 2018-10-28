package me.mingshan.demo.disruptor.demo3;

import com.lmax.disruptor.EventTranslator;
import me.mingshan.demo.disruptor.Element;

import java.util.Random;

public class TradeTransactionEventTranslator implements EventTranslator<Element> {

    @Override
    public void translateTo(Element event, long sequence) {
        event.setValue(9999);
    }
}
