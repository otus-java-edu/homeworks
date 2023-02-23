package atm;

import atm.Exceptions.InsufficientBalanceException;

public class ATM implements AtmInterface {
    private int balance = 0;

    @Override
    public void replenish(BanknoteType banknoteType, int count) {
        if (count < 1)
            throw new IllegalArgumentException("Unable to replenish negative value of banknotes.");
        balance += banknoteType.getValue() * count;
    }

    @Override
    public Money getMoney(int count) {
        var orderedBanknoteTypes = BanknoteType.orderedBanknoteTypesDesc;
        if (count % BanknoteType.orderedBanknoteTypesDesc.get(BanknoteType.orderedBanknoteTypesDesc.size() - 1).getValue() != 0)
            throw new IllegalArgumentException("Unable to get such count of money using available banknotes.");
        if (count > balance)
            throw new InsufficientBalanceException("Not enough balance.");

        var money = new Money();
        var needToProvideCount = count;
        for (BanknoteType banknoteType : orderedBanknoteTypes) {
            var banknotesCount = needToProvideCount / banknoteType.getValue();
            if (banknotesCount > 0) {
                money.addBanknotes(banknoteType, banknotesCount);
                needToProvideCount -= banknotesCount * banknoteType.getValue();
            }
        }
        balance -= count;
        return money;
    }

    @Override
    public int getBalance() {
        return balance;
    }
}
