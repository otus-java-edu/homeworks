package atm.interfaces;

import atm.BanknoteType;
import atm.Cash;

public interface ATM {
    void replenish(BanknoteType banknoteType, int count);
    Cash getMoney(int count);
    int getBalance();
}
