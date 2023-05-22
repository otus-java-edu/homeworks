package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final ConcurrentSkipListSet<SensorData> dataBuffer = new ConcurrentSkipListSet<>();
    private final SensorDataBufferedWriter writer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        synchronized (dataBuffer) {
            dataBuffer.add(data);
        }

        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public void flush() {
        try {
            List<SensorData> data;
            synchronized (dataBuffer) {
                if (dataBuffer.size() == 0)
                    return;
                data = dataBuffer.stream().toList();
                dataBuffer.clear();
            }
            writer.writeBufferedData(data);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
