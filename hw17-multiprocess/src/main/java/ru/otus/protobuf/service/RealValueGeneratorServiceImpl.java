package ru.otus.protobuf.service;

public class RealValueGeneratorServiceImpl implements RealValuesGeneratorService {
    private int lastValue;
    private int currentValue;

    public RealValueGeneratorServiceImpl() {
    }

    @Override
    public void init(int firstValue, int lastValue) {
        this.lastValue = lastValue;
        this.currentValue = firstValue;
    }

    @Override
    public int getNext() {
        if (currentValue > lastValue)
            return Integer.MIN_VALUE;
        return currentValue++;
    }

    @Override
    public boolean isFinished() {
        return currentValue > lastValue;
    }
}
