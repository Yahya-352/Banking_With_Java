import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bank {

    private List<User> users;
    FileHandler fileHandler;
    private List<Account> accounts;
    private int failedAttempts;

    public Bank() throws IOException {
        fileHandler = new FileHandler();
        accounts = fileHandler.accountsListLoading();
        users = fileHandler.userListLoading();
    }

    public User login(String username , String password){

        Optional<User> foundUser = users.stream().filter
                (u -> u.getUsername().equalsIgnoreCase(username)).findFirst();

        if(foundUser.isEmpty()){
            throw new IllegalArgumentException("User with username not found : " + username);
        }

        User user = foundUser.get();
        if(user.isLocked()){
            throw new IllegalArgumentException("too many failed attempts .. try again in a minute");
        }

        if(!user.checkPassword(password)){
            user.registerFailedAttempt();
            throw new IllegalArgumentException("Password not correct");
        }
        user.resetFailedAttempts();
        return user;
    }

    public Customer addCustomer(String username , String password , boolean wantsChecking , boolean wantsSaving){

        boolean alreadyExists = users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));

        if(alreadyExists){
            throw new IllegalArgumentException("username already exists!");
        }
        if(!wantsChecking && !wantsSaving){
            throw new IllegalArgumentException("Customer must have at least one account");
        }

        Customer newCustomer = new Customer(username,password);
        if(wantsChecking){
            CheckingAccount checkingAccount = new CheckingAccount("" , 0);
            newCustomer.setCheckingAccount(checkingAccount);
            accounts.add(checkingAccount);
        }
        if(wantsSaving){
            SavingsAccount savingsAccount = new SavingsAccount("" , 0);
            newCustomer.setSavingsAccount(savingsAccount);
            accounts.add(savingsAccount);
        }

        users.add(newCustomer);
        fileHandler.saveAccounts(accounts);
        fileHandler.saveUsers(users);
        return newCustomer;
    }

    public void withdraw(Account account, double amount) {
        account.withDraw(amount);

        Transaction transaction = new Transaction(
                account.getAccountNumber(),
                "WITHDRAW",
                amount,
                account.getBalance(),
                LocalDateTime.now()
        );
        fileHandler.appendingTransactions(transaction);
        fileHandler.saveAccounts(accounts);
    }

    public void deposit(Account account, double amount) {
        account.deposit(amount);

        Transaction transaction = new Transaction(
                account.getAccountNumber(),
                "DEPOSIT",
                amount,
                account.getBalance(),
                LocalDateTime.now()
        );

        fileHandler.appendingTransactions(transaction);
        fileHandler.saveAccounts(accounts);
    }
}
