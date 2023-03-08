package ru.otus.processor;

import ru.otus.TimeSecondProvider;
import ru.otus.exceptions.SecondException;
import ru.otus.model.Message;

public interface Processor {

    Message process(Message message);

    default void processTime(TimeSecondProvider secondProvider){
        if (secondProvider.getTime().getSecond() % 2 == 0)
            throw new SecondException();
    }
}
