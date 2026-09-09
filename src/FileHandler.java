import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {

    private static final String USERS_FILE = "users.txt";
    private File file;

    public FileHandler() throws IOException{
        file = new File(USERS_FILE);

        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
        } else {
            System.out.println("File already exists.");
        }
    }

    public List<User> userListLoading(){
        ArrayList<User> users = new ArrayList<>();
        try  {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                String[] parts = data.split("\\|");
                String username = parts[0];
                String password = parts[1];
                String role = parts[2];

                User user;
                if (role.equals("BANKER")) {
                    user = new Banker(username, password);
                } else {
                    user = new Customer(username, password);
                }
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return users;
    }
    public void saveUsers(List<User> users){
        try {
            FileWriter myWriter = new FileWriter(USERS_FILE);
            for(int i = 0 ; i < users.size();i++){
                String username = users.get(i).getUsername();
                String pass = users.get(i).getPassword();
                String role = users.get(i).getRole();
                myWriter.write(username + "|" + pass + "|" + role + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
