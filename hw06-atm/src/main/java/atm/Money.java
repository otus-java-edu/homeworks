package atm;

import java.util.HashMap;

public class Money {
    private final HashMap<BanknoteType, Integer> moneyMap = new HashMap<>();

    public void addBanknotes(BanknoteType type, int count){
        if (count < 1)
            throw new IllegalArgumentException("Unable to add negative value of banknotes.");
        if (!moneyMap.containsKey(type))
            moneyMap.put(type, count);
        else
            moneyMap.replace(type, moneyMap.get(type) + count);
    }

    public HashMap<BanknoteType, Integer> getMoneyMap(){return moneyMap;}

    public int count() {
        return moneyMap.entrySet().stream().mapToInt(pair -> pair.getKey().getValue() * pair.getValue()).sum();
    }
}
