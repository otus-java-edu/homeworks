package atm.tests;

import atm.ATM;
import atm.BanknoteType;
import atm.Exceptions.InsufficientBalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ATMTests {

    @Test
    @DisplayName("Проверяем, что изначально баланс = 0")
    void initialBalanceTest() {
        var atm = new ATM();
        assertThat(atm.getBalance()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверяем, что можно положить деньги и верность баланса")
    void replenishTest() {
        //given
        var atm = new ATM();

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
        var atm = new ATM();
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
        var atm = new ATM();
        atm.replenish(BanknoteType.FIVE_HUNDRED, 2);
        atm.replenish(BanknoteType.HUNDRED, 3);
        atm.replenish(BanknoteType.THOUSAND, 4);
        atm.replenish(BanknoteType.FIVE_THOUSAND, 5);

        assertThrows(InsufficientBalanceException.class, () ->
                atm.getMoney(30400));
    }
}
