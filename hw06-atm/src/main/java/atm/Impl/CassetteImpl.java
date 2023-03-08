package atm.Impl;

import atm.Exceptions.InsufficientCapacityException;
import atm.Exceptions.InsufficientVolumeException;
import atm.Cassette;

public class CassetteImpl implements Cassette {
    private final int capacity;
    private int count;

    public CassetteImpl(int capacity, int count) {
        this.capacity = capacity;
        this.count = count;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getAvailableVolume() {
        return count;
    }

    @Override
    public int getFreeSpace() {
        return capacity - count;
    }

    @Override
    public void add(int count) {
        if (count < 0)
            throw new IllegalArgumentException("Unable to add negative value of banknotes.");
        if (capacity - this.count - count < 0)
            throw new InsufficientCapacityException("Unable to get such count of banknotes");
        this.count += count;
    }

    @Override
    public int get(int count) {
        if (count < 0)
            throw new IllegalArgumentException("Unable to get negative value of banknotes.");
        if (this.count < count)
            throw new InsufficientVolumeException("Not enough banknotes");
        this.count -= count;
        return count;
    }
}
