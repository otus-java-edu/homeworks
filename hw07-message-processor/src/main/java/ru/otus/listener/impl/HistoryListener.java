package ru.otus.listener.impl;

import ru.otus.listener.HistoryReader;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> historyMap;
    public HistoryListener(){
        historyMap = new HashMap<>();
    }
    @Override
    public void onUpdated(Message msg) {
        historyMap.put(msg.getId(), msg.toBuilder().build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(historyMap.get(id));
    }
}
