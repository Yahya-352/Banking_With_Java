import java.time.LocalDateTime;

public abstract class User {
    private String username;
    private String password;
    protected int failedAttempts = 0;
    protected LocalDateTime lockedUntil;

    public User(String username , String password){
        this.username = username;
        this.password = password;
    }

    public boolean isLocked(){
        if(lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil)){
            return true;
        }
        return false;
    }

    public void registerFailedAttempt(){
        failedAttempts++;
        if(failedAttempts >= 3){
            lockedUntil = LocalDateTime.now().plusSeconds(60);
        }
    }

    public void resetFailedAttempts(){
        failedAttempts = 0;
        lockedUntil = null;
    }

    public boolean checkPassword(String inputPass){
        return password.equals(inputPass);
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public abstract String getRole();
}
