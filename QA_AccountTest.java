package root.account;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AccountTest {
    private Account account;
    private int bound = 1000000;


    @BeforeMethod
    public void beforeEachMethod() {
        account = new Account();
    }

    @Test
    public void firstTest() {
        assertFalse(account.isBlocked(), "Созданный аккаунт не заблокирован");
        assertEquals(account.getBalance(), 0, "Проверка баланса");
        assertTrue(account.getMaxCredit() <= bound, "Кредитный максимум не превышает 1000000");
        assertTrue(account.getMaxCredit() >= -bound, "Кредитный максимум не ниже -1000000");
        assertTrue(account.getMaxCredit() >= a.getBalance(), "Кредитный макимум не больше баланса");
    }

    @Test(dependsOnMethods = "firstTest")
    public void blockingTest() {
        account.block();
        assertTrue(account.isBlocked(), "Аккаунт должен быть заблокирован");
        assertTrue(account.unblock(), "Аккаунт должен разблокироваться");
        assertFalse(account.isBlocked(), "Аккаунт должен быть разблокирован");
    }

    @Test(dependsOnMethods = {"firstTest", "blockingTest"})
    public void maxCreditTest() {
        int lastMax = account.getMaxCredit();
        assertFalse(account.setMaxCredit(10000), "Нельзя менять максимум в незаблокированном состоянии");
        assertEquals(account.getMaxCredit(), lastMAx, "Максимум не должен измениться");
        account.block();
        assertTrue(account.setMaxCredit(10000), "Можно менять максимум");
        assertEquals(account.getMaxCredit(), 10000, "Максимум должен измениться");
        account.unblock();
        account.setMaxCredit(bound);
    }

    @Test(dependsOnMethods = {"firstTest", "blockingTest", "maxCreditTest"})
    public void depositTest() {
        assertFalse(account.deposit(-1000), "Нельзя сделать депозит отрицательным");
        assertTrue(account.deposit(1000), "Депозит должен быть успешно изменен");
        assertEquals(account.getBalance(), 1000, "Новое значение депозита");
        account.block();
        assertFalse(account.deposit(2000), "Нельзя менять депозит в блокировке");
        maxCredit = account.getMaxCredit();
        account.unblock();
        assertEquals(account.deposit(maxCredit + 1),  1000, "Нельзя делать депозит больше максимума");
    }

    @Test(dependsOnMethods = {"firstTest", "blockingTest", "maxCreditTest", "depositTest"})
    public void withdrawTest() {
        int balance = account.getBalance();
        assertFalse(account.withdraw(-1000), "Нельзя снять отрицательную сумму денег");
        assertTrue(account.withdraw(1000), "Снятие прошло успешно");
        assertEquals(account.getBalance(), balance - 1000, "Баланс изменен");
        account.block();
        assertFalse(account.withdraw(1000), "Нельзя снимать деньги в блокировке");
        account.unblock();
        assertFalse(account.withdraw(bound + 1), "Нельзя снять денег больше, чем лимит");
        assertEquals(account.getBalance(), balance - 1000, "Баланс не должен измениться");
    }
}
