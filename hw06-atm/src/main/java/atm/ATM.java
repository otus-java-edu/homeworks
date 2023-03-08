package atm;

public interface ATM {
    void replenish(BanknoteType banknoteType, int count);
    Cash getMoney(int count);
    int getBalance();
}
