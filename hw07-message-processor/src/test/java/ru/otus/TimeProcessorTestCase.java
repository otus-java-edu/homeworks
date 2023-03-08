package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.exceptions.SecondException;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.impl.ProcessorConcatFields;
import ru.otus.processor.impl.TimeProcessor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimeProcessorTestCase {
    private TimeProcessor processor;
    @Test
    public void testOddEvenSecond(){
        processor = new TimeProcessor(()->LocalDateTime.of(1,1,1,1,2,2));
        assertThrows(SecondException.class, () ->
                processor.process(new Message.Builder(1).build()));
        processor = new TimeProcessor(()->LocalDateTime.of(1,1,1,1,2,3));
        processor.process(new Message.Builder(1).build());
    }
}
