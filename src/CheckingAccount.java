public class CheckingAccount extends Account{

    public CheckingAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public String getAccountType() {
        return "CHECKING ACCOUNT";
    }
}
