package ru.otus.protobuf.service;

public interface RealValuesGeneratorService {
    void init(int firstValue, int lastValue);
    int getNext();
    boolean isFinished();
}
