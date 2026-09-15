import java.io.IOException;
import java.time.LocalDate;
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
        reconnectAccountsAfterReload();
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
            CheckingAccount checkingAccount = new CheckingAccount(Account.generateAccountNumber() , 0);
            checkingAccount.setCustomerId(newCustomer.getUserId());
            newCustomer.setCheckingAccount(checkingAccount);
            accounts.add(checkingAccount);
        }
        if(wantsSaving){
            SavingsAccount savingsAccount = new SavingsAccount(Account.generateAccountNumber() , 0);
            savingsAccount.setCustomerId(newCustomer.getUserId());
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

    private void reconnectAccountsAfterReload(){
        for(int i = 0 ; i < users.size() ; i++){
            User user = users.get(i);
            if(user.getRole().equals("CUSTOMER")){
                Customer customer = (Customer) user;
                for(int j = 0; j < accounts.size() ; j++){
                    if(customer.getUserId().equals(accounts.get(i).getCustomerId())){
                        if(accounts.get(i).getAccountType().equals("CHECKING_ACCOUNT")){
                            customer.setCheckingAccount((CheckingAccount) accounts.get(j));
                        }else{
                            customer.setSavingsAccount((SavingsAccount) accounts.get(j));
                        }
                    }
                }
            }
        }
    }

    public List<Transaction> getTransactionHistory(Account account){
        List<Transaction> transactions = fileHandler.transactionsListLoading();
        return transactions.stream().filter(a -> a.getAccountNumber()
                .equals(account.getAccountNumber())).toList();
    }


    //transaction filtering methods..

    public List<Transaction> getTransactionsForToday(List<Transaction> transactions){
        return transactions.stream()
                .filter(t -> t.getTimestamp().toLocalDate().equals(LocalDate.now()))
                .toList();
    }

    public List<Transaction> getTransactionsForYesterday(List<Transaction> transactions){
        return transactions.stream()
                .filter(t -> t.getTimestamp().toLocalDate().equals(LocalDate.now().minusDays(1)))
                .toList();
    }

    public List<Transaction> getTransactionsForLast7Days(List<Transaction> transactions){
        return transactions.stream().filter(t -> t.getTimestamp()
                .isAfter(LocalDateTime.now().minusDays(7))).toList();
    }
    public List<Transaction> getTransactionsForLast30Days(List<Transaction> transactions){
        return transactions.stream().filter(t -> t.getTimestamp()
                .isAfter(LocalDateTime.now().minusDays(30))).toList();
    }

    public List<Transaction> getTransactionsByMonth(List<Transaction> transactions , int month){
        return transactions.stream().filter(t ->
                t.getTimestamp().getMonthValue() == month).toList();
    }

    public List<Transaction> getTransactionsForCustomDates(List<Transaction> transactions ,
                                                         LocalDate startDate , LocalDate endDate){
        return transactions.stream().filter(t -> t.getTimestamp().toLocalDate().isAfter(startDate)&&
                t.getTimestamp().toLocalDate().isBefore(endDate)).toList();
    }
}
