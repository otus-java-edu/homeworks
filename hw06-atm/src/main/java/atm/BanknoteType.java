package atm;

import java.util.Arrays;
import java.util.List;

public enum BanknoteType {
    FIVE_THOUSAND("Пять тысяч", 5000),
    THOUSAND("Тысяча", 1000),
    FIVE_HUNDRED("Пятьсот", 500),
    HUNDRED("Сто", 100);
    private final String title;
    private final int value;

    public static final List<BanknoteType> orderedBanknoteTypesDesc = Arrays.asList(FIVE_THOUSAND, THOUSAND, FIVE_HUNDRED, HUNDRED);

    BanknoteType(String title, int value){
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public int getValue() {
        return value;
    }
    @Override
    public String toString() {
        return title;
    }
}
