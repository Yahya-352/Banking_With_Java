import java.time.LocalDateTime;

public abstract class User {
    private String username;
    private String password;
    protected int failedAttempts;
    protected LocalDateTime lockedUntil;

    public User(String username , String password){
        this.username = username;
        this.password = password;
    }

    public boolean checkPassword(String inputPass){
        return password.equals(inputPass);
    }

    public String getUsername() {
        return username;
    }

    public abstract String getRole();
}
