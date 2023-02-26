package atm;

public interface AtmInterface {
    void replenish(BanknoteType banknoteType, int count);
    Money getMoney(int count);
    int getBalance();
}
