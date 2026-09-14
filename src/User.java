import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

    public void hashingPassword(String passwordToHash) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);

        byte[] hashedPassword = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
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
