import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bank {

    private List<User> users;

    public Bank(){
        users = new ArrayList<>();
    }

    public User login(String username , String password){

        Optional<User> foundUser = users.stream().filter
                (u -> u.getUsername().equalsIgnoreCase(username)).findFirst();

        if(foundUser.isEmpty()){
            throw new IllegalArgumentException("User with username not found : " + username);
        }

        User user = foundUser.get();

        if(!user.checkPassword(password)){
            throw new IllegalArgumentException("Password not correct");
        }
        return user;
    }

    public Customer addCustomer(String username , String password , boolean wantsChecking , boolean wantsSaving){

        boolean alreadyExists = users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));

        if(alreadyExists){
            throw new IllegalArgumentException("username already exists!");
        }
        if(!wantsChecking && !wantsSaving){
            throw new IllegalArgumentException("Customer must have atleast one account");
        }

        Customer newCustomer = new Customer(username,password);
        if(wantsChecking){
            CheckingAccount checkingAccount = new CheckingAccount("" , 0);
            newCustomer.setCheckingAccount(checkingAccount);
        }
        if(wantsSaving){
            SavingsAccount savingsAccount = new SavingsAccount("" , 0);
            newCustomer.setSavingsAccount(savingsAccount);
        }

        users.add(newCustomer);
        return newCustomer;
    }
}
