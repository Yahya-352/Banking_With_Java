public class Customer extends User{

    private CheckingAccount checkingAccount;
    private SavingsAccount savingsAccount;

    public Customer(String username, String password) {
        super(username, password);
    }

    public void setCheckingAccount(CheckingAccount checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public void setSavingsAccount(SavingsAccount savingsAccount) {
        this.savingsAccount = savingsAccount;
    }

    public CheckingAccount getCheckingAccount() {
        return checkingAccount;
    }

    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }
}
