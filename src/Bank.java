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

    public Customer addCustomer(String username , String password , boolean wantsChecking
            , boolean wantsSaving , String checkingCardType , String savingsCardType){

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
            checkingAccount.setCard(createCard(checkingCardType));
            newCustomer.setCheckingAccount(checkingAccount);
            accounts.add(checkingAccount);
        }
        if(wantsSaving){
            SavingsAccount savingsAccount = new SavingsAccount(Account.generateAccountNumber() , 0);
            savingsAccount.setCustomerId(newCustomer.getUserId());
            savingsAccount.setCard(createCard(savingsCardType));
            newCustomer.setSavingsAccount(savingsAccount);
            accounts.add(savingsAccount);
        }

        users.add(newCustomer);
        fileHandler.saveAccounts(accounts);
        fileHandler.saveUsers(users);
        return newCustomer;
    }

    public static ICard createCard(String cardType){
        if(cardType.equals("PLATINUM")){
            return new MastercardPlatinum();
        }else if(cardType.equals("TITANIUM")){
            return new MastercardTitanium();
        }else{
            return new Mastercard();
        }
    }

    public void withdraw(Account account, double amount) {
        List<Transaction> todayTransactions = getTransactionsForToday(getTransactionHistory(account));
        double withdrawnToday = todayTransactions.stream().filter
                (a -> a.getType().equals("WITHDRAW")).mapToDouble(Transaction::getAmount).sum();

        if(withdrawnToday + amount > account.getCard().getWithdrawLimit()){
            throw new IllegalArgumentException("This would exceed your daily withdrawal limit of $"
                    + account.getCard().getWithdrawLimit());
        }
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

        List<Transaction> todayTransactions = getTransactionsForToday(getTransactionHistory(account));
        double depositedToday = todayTransactions.stream().filter
                (a -> a.getType().equals("DEPOSIT")).mapToDouble(Transaction::getAmount).sum();

        if(depositedToday + amount > account.getCard().getDepositLimit()){
            throw new IllegalArgumentException("This would exceed your daily deposit limit of $"
                    + account.getCard().getDepositLimit());
        }

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

    public void transfer(Account fromAcc, String toAccNumber, double amount) {
        Account toAccount = null;

        if (fromAcc.getAccountNumber().equals(toAccNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber().equals(toAccNumber)) {
                toAccount = accounts.get(i);
            }
        }

        if (toAccount == null) {
            throw new IllegalArgumentException("No account found with that number: " + toAccNumber);
        }

        String transferType = "TRANSFER_OUT";
        if (toAccount.getCustomerId().equals(fromAcc.getCustomerId())) {
            transferType = "TRANSFER_OUT_INTERNAL";
        }

        List<Transaction> todayTransactions = getTransactionsForToday(getTransactionHistory(fromAcc));

        if (toAccount.getCustomerId().equals(fromAcc.getCustomerId())) {
            double transferedToday = todayTransactions.stream()
                    .filter(a -> a.getType().equals("TRANSFER_OUT_INTERNAL"))
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            if (transferedToday + amount > fromAcc.getCard().getOwnTransferLimit()) {
                throw new IllegalArgumentException("This would exceed your daily transfer limit(Internal) of $"
                        + fromAcc.getCard().getOwnTransferLimit());
            }
        } else {
            double transferedToday = todayTransactions.stream()
                    .filter(a -> a.getType().equals("TRANSFER_OUT"))
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            if (transferedToday + amount > fromAcc.getCard().getTransferLimit()) {
                throw new IllegalArgumentException("This would exceed your daily transfer limit(General) of $"
                        + fromAcc.getCard().getTransferLimit());
            }
        }

        fromAcc.withDraw(amount);
        toAccount.deposit(amount);

        Transaction outTransaction = new Transaction(
                fromAcc.getAccountNumber(), transferType, amount, fromAcc.getBalance(), LocalDateTime.now());
        Transaction inTransaction = new Transaction(
                toAccount.getAccountNumber(), "TRANSFER_IN", amount, toAccount.getBalance(), LocalDateTime.now());

        fileHandler.appendingTransactions(outTransaction);
        fileHandler.appendingTransactions(inTransaction);
        fileHandler.saveAccounts(accounts);
    }

    private void reconnectAccountsAfterReload(){
        for(int i = 0 ; i < users.size() ; i++){
            User user = users.get(i);
            if(user.getRole().equals("CUSTOMER")){
                Customer customer = (Customer) user;
                for(int j = 0; j < accounts.size() ; j++){
                    if(customer.getUserId().equals(accounts.get(j).getCustomerId())){
                        if(accounts.get(j).getAccountType().equals("CHECKING_ACCOUNT")){
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
