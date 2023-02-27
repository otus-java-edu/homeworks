package atm.Impl;

import atm.BanknoteType;
import atm.Cash;
import atm.Exceptions.InsufficientCapacityException;
import atm.Exceptions.InsufficientVolumeException;
import atm.ATM;
import atm.Cassette;

import java.util.HashMap;

public class ATMImpl implements ATM {
    private int balance = 0;

    private final HashMap<BanknoteType, Cassette> cassettes;

    public ATMImpl(int casseteCapacity, int casseteCount) {
        cassettes = new HashMap<>();
        for (var type : BanknoteType.values()) {
            cassettes.put(type, new CassetteImpl(casseteCapacity, casseteCount));
        }
    }

    @Override
    public void replenish(BanknoteType banknoteType, int count) {
        if (count < 1)
            throw new IllegalArgumentException("Unable to replenish negative value of banknotes.");
        if (!canReplenish(banknoteType, count))
            throw new InsufficientCapacityException("Unable to replenish banknote type to cassete: " + banknoteType.getTitle());
        cassettes.get(banknoteType).add(count);
        balance += banknoteType.getValue() * count;
    }

    @Override
    public Cash getMoney(int count) {
        var orderedBanknoteTypes = BanknoteType.orderedBanknoteTypesDesc;
        if (count % BanknoteType.orderedBanknoteTypesDesc.get(BanknoteType.orderedBanknoteTypesDesc.size() - 1).getValue() != 0)
            throw new IllegalArgumentException("Unable to get such count of money using available banknotes.");
        if (count > balance)
            throw new InsufficientVolumeException("Not enough balance.");

        var money = new Cash();
        var needToProvideCount = count;
        for (BanknoteType banknoteType : orderedBanknoteTypes) {
            var banknotesCount = needToProvideCount / banknoteType.getValue();
            if (banknotesCount > 0 && cassettes.containsKey(banknoteType)) {
                var availableBankNotes = cassettes.get(banknoteType).getAvailableVolume();
                if (availableBankNotes < banknotesCount)
                    banknotesCount = availableBankNotes;
                cassettes.get(banknoteType).get(banknotesCount);
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

    private boolean canReplenish(BanknoteType type, int count){
        return cassettes.containsKey(type) && cassettes.get(type).getFreeSpace() >= count;
    }
}
