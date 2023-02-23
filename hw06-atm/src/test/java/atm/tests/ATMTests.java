package atm.tests;

import atm.ATMImpl;
import atm.BanknoteType;
import atm.Exceptions.InsufficientCapacityException;
import atm.Exceptions.InsufficientVolumeException;
import atm.interfaces.ATM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ATMTests {

    private ATM atm;
    @BeforeEach
    void Setup()
    {
        atm = new ATMImpl(100, 100);
    }

    @Test
    @DisplayName("Проверяем, что изначально баланс = 0")
    void initialBalanceTest() {
        assertThat(atm.getBalance()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверяем, что можно положить деньги и верность баланса")
    void replenishTest() {
        //given

        //when

        atm.replenish(BanknoteType.FIVE_HUNDRED, 1);
        atm.replenish(BanknoteType.HUNDRED, 2);
        atm.replenish(BanknoteType.THOUSAND, 3);
        atm.replenish(BanknoteType.FIVE_THOUSAND, 4);

        //then
        assertThat(atm.getBalance()).isEqualTo(23700);
    }

    @Test
    @DisplayName("Проверяем, что можно положить и снять деньги")
    void replenishAndGetMoneyTest() {
        //given
        atm.replenish(BanknoteType.FIVE_HUNDRED, 2);
        atm.replenish(BanknoteType.HUNDRED, 3);
        atm.replenish(BanknoteType.THOUSAND, 4);
        atm.replenish(BanknoteType.FIVE_THOUSAND, 5);

        //when
        var money = atm.getMoney(30300);

        //then
        assertThat(money.count()).isEqualTo(30300);
    }

    @Test
    @DisplayName("Проверяем, что нельзя снять деньги больше чем на балансе")
    void replenishAndGetMoreMoneyTest() {
        atm.replenish(BanknoteType.FIVE_HUNDRED, 2);
        atm.replenish(BanknoteType.HUNDRED, 3);
        atm.replenish(BanknoteType.THOUSAND, 4);
        atm.replenish(BanknoteType.FIVE_THOUSAND, 5);

        assertThrows(InsufficientVolumeException.class, () ->
                atm.getMoney(30400));
    }

    @Test
    @DisplayName("Проверяем, что нельзя положить деньег больше чем размер кассеты")
    void replenishTooMuchBanknotesTest() {
        assertThrows(InsufficientCapacityException.class, () ->
                atm.replenish(BanknoteType.FIVE_HUNDRED, 200));
    }}
