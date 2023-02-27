package atm;

public interface Cassette {
    int getCapacity();
    int getAvailableVolume();
    int getFreeSpace();
    void add(int count);
    int get(int count);
}
