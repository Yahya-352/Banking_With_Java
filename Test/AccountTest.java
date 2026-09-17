import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private CheckingAccount account;
    @BeforeEach
    void setUp(){
        account = new CheckingAccount("ACCTESTING" , 0);
        account.setCard(new Mastercard());
    }

    @Test
    void depositIncreaseBalance(){
        account.deposit(100);
        assertEquals(100 , account.getBalance());
    }

    @Test
    void WithdrawDecreaseBalance(){
        account.deposit(500);
        account.withDraw(200);
        assertEquals(300,account.getBalance());
    }

    @Test
    void withdrawTriggersOverdraftFeeIfBalanceIsNegative() {
        account.deposit(50);
        account.withDraw(100);

        assertEquals(-85, account.getBalance());
        assertEquals(1, account.getOverDraftCount());
    }

    @Test
    void WithdrawMoreThan100BlockedIfNegativeBalance(){
        account.deposit(60);
        account.withDraw(120);

        assertThrows(IllegalArgumentException.class, () -> account.withDraw(150));
    }

    @Test
    void accountDeactivatesAfterTwoOverdrafts() {
        account.deposit(50);
        account.withDraw(100);
        account.withDraw(80);

        assertEquals(false, account.isActive());
        assertEquals(2, account.getOverDraftCount());
    }

    @Test
    void withdrawOnDeactivatedAccountThrows() {
        account.deposit(50);
        account.withDraw(100);
        account.withDraw(80);

        assertThrows(IllegalArgumentException.class, () -> account.withDraw(10));
    }

    @Test
    void accountReactivatesWhenBalanceGetsBackToPositve(){
        account.deposit(50);
        account.withDraw(100);
        account.withDraw(80);

        assertEquals(false,account.isActive());

        account.deposit(300);
        assertEquals( true , account.isActive());
        assertEquals(0, account.getOverDraftCount());
    }
}