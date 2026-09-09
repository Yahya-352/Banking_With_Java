public class Banker extends User{

    public Banker(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "Banker";
    }

}
