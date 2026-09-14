import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Bank bank = new Bank();

        Customer alice = bank.addCustomer("yahya1", "yahya123", true, false);
        Account checking = alice.getCheckingAccount();

        System.out.println("Account number:" + checking.getAccountNumber());

        bank.deposit(checking, 500);
        bank.withdraw(checking, 100);

        List<Transaction> history = bank.getTransactionHistory(checking);

        System.out.println("Transaction History");
        history.forEach(System.out::println);
    }
}