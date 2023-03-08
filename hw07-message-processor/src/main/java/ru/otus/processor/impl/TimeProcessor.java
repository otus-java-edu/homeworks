package ru.otus.processor.impl;

import ru.otus.TimeSecondProvider;
import ru.otus.exceptions.SecondException;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class TimeProcessor implements Processor {

    private final TimeSecondProvider timeSecondProvider;
    public TimeProcessor(TimeSecondProvider timeSecondProvider){
        this.timeSecondProvider = timeSecondProvider;
    }
    @Override
    public Message process(Message message) {
        if (timeSecondProvider.getTime().getSecond() % 2 == 0)
            throw new SecondException();
        return message;
    }
}
