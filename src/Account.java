public abstract class Account implements Transactable{
    private String accountNumber;
    private double balance;
    private boolean isActive;
    private int overDraftCount = 0;


    public Account(String accountNumber , double balance){
        this.accountNumber = accountNumber;
        this.balance = balance;
        isActive = true;
    }

    public void withDraw(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Cannot deposit a negative or zero amount");
        }
        if(!isActive){
            throw new IllegalArgumentException("Following account is not active");
        }
        if(balance < 0  && amount > 100){
            throw new IllegalArgumentException("You are not allowed to withdraw more than 100 when your" +
                    "balance is already negative");
        }

        balance -= amount;

        if(balance < 0){
            balance -= 35;
            overDraftCount++;

            if (overDraftCount >= 2){
                setActive(false);
                System.out.println("Account deactivated due to overdraft count getting above 2");
            }
        }

    }

    public void deposit(double amount){
        if(amount <= 0 ){
            throw new IllegalArgumentException("Cannot deposit a negative or zero amount");
        }
        balance += amount;

        if(!isActive && balance >=0){
            setActive(true);
            overDraftCount = 0;
            System.out.println("Account reactivated and overdraft count has been reset to zero");
        }

    }

    public void transfer(Account targetAccount , double amount){

    }

    public int getOverDraftCount() {
        return overDraftCount;
    }


    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public abstract String getAccountType();
}
