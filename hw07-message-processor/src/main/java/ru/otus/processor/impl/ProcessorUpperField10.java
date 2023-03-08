package ru.otus.processor.impl;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorUpperField10 implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder().field4(message.getField10().toUpperCase()).build();
    }
}
