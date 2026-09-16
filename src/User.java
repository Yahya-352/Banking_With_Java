
import java.time.LocalDateTime;

public abstract class User {
    private String username;
    private String password;
    protected int failedAttempts = 0;
    protected LocalDateTime lockedUntil;
    private String userId;

    public User(String username , String password){
        this.username = username;
        this.password = PasswordUtil.hashFunction(password);
        userId = "USER" + System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return password.equals(PasswordUtil.hashFunction(inputPass));
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public abstract String getRole();
}
