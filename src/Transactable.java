public interface Transactable {

    public void withDraw(double amount);
    public void deposit(double amount);
    public void transfer(Account targetAccount , double amount);

}



