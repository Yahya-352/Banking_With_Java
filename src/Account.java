public abstract class Account {
    private String accountNumber;
    private double balance;

    public Account(String accountNumber , double balance){
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public abstract String getAccountType();
}
