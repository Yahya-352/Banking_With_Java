public class SavingsAccount extends Account{

    public SavingsAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public String getAccountType() {
        return "SAVINGS ACCOUNT";
    }

}
