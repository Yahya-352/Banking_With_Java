import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Bank bank = new Bank();

        bank.addCustomer("alice", "alice123", true, false);
        System.out.println("Customer created.");

        User loggedIn = bank.login("alice", "alice123");
        System.out.println("Login success! Welcome " + loggedIn.getUsername() + " (" + loggedIn.getRole() + ")");

        try {
            bank.login("alice", "wrongpass");
        } catch (IllegalArgumentException e) {
            System.out.println("Correctly rejected bad password: " + e.getMessage());
        }
    }
}
