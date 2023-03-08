package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.exceptions.SecondException;
import ru.otus.processor.Processor;
import ru.otus.processor.ProcessorConcatFields;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcessorTestCase {
    private Processor processor;
    @BeforeEach
    public void initTest(){
        processor = new ProcessorConcatFields();
    }
    @Test
    public void testOddEvenSecond(){
        assertThrows(SecondException.class, () ->
                processor.processTime(()->LocalDateTime.of(1,1,1,1,2,2)));
        processor.processTime(()->LocalDateTime.of(1,1,1,1,2,3));
    }
}
