import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {

    private static final String USERS_FILE = "users.txt";
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    public FileHandler() throws IOException{
        createFiles(USERS_FILE);
        createFiles(ACCOUNTS_FILE);
        createFiles(TRANSACTIONS_FILE);
    }

    public void createFiles(String path) throws IOException {
        File file = new File(path);
        if(file.createNewFile()){
            System.out.println("File created : " + file.getName());
        }else{
            System.out.println(path + " file already exists");
        }
    }

    public List<User> userListLoading(){
        ArrayList<User> users = new ArrayList<>();
        try  {
            Scanner myReader = new Scanner(new File(USERS_FILE));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                String[] parts = data.split("\\|");
                String username = parts[0];
                String password = parts[1];
                String role = parts[2];
                String userId = parts[3];

                User user;
                if (role.equals("BANKER")) {
                    user = new Banker(username, password);
                } else {
                    user = new Customer(username, password);
                }
                user.setUserId(userId);
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
                String userId = users.get(i).getUserId();

                myWriter.write(username + "|" + pass + "|" + role + "|" + userId + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public List<Account> accountsListLoading(){
        ArrayList<Account> accounts = new ArrayList<>();
        try  {
            Scanner myReader = new Scanner(new File(ACCOUNTS_FILE));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                String[] parts = data.split("\\|");
                String accountNumber = parts[0];
                double balance = Double.parseDouble(parts[1]);
                int overDraftCount = Integer.parseInt(parts[2]);
                boolean isActive = Boolean.parseBoolean(parts[3]);
                String accountType = parts[4];
                String customerId = parts[5];
                String cardType = parts[6];

                Account account;

                if (accountType.equals("CHECKING_ACCOUNT")) {
                    account = new CheckingAccount(accountNumber, balance);
                } else {
                    account = new SavingsAccount(accountNumber, balance);
                }
                account.setCustomerId(customerId);
                account.setActive(isActive);
                account.setOverDraftCount(overDraftCount);
                account.setCard(Bank.createCard(cardType));
                accounts.add(account);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return accounts;
    }


    public void saveAccounts(List<Account> accounts){
        try {
            FileWriter myWriter = new FileWriter(ACCOUNTS_FILE);
            for (int i = 0 ; i < accounts.size() ; i++){
                String accountNumber = accounts.get(i).getAccountNumber();
                double balance = accounts.get(i).getBalance();
                int overDraftCount = accounts.get(i).getOverDraftCount();
                boolean isActive = accounts.get(i).isActive();
                String accountType = accounts.get(i).getAccountType();
                String customerId = accounts.get(i).getCustomerId();
                String cardType = accounts.get(i).getCard().getCardType();
                myWriter.write(accountNumber + "|" + balance + "|"
                        + overDraftCount + "|" + isActive + "|" + accountType
                        + "|" + customerId + "|" + cardType +"\n");
            }
            myWriter.close();
        }catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    public List<Transaction> transactionsListLoading(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        try  {
            Scanner myReader = new Scanner(new File(TRANSACTIONS_FILE));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                String[] parts = data.split("\\|");
                String accountNumber = parts[0];
                String type = parts[1];
                double amount = Double.parseDouble(parts[2]);
                double balanceAfter = Double.parseDouble(parts[3]);
                LocalDateTime timeStamp = LocalDateTime.parse(parts[4]);
                Transaction transaction = new Transaction(accountNumber, type ,
                         amount,balanceAfter , timeStamp);

                transactions.add(transaction);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return transactions;
    }

    public void appendingTransactions(Transaction transaction){
        try{
            FileWriter writer = new FileWriter(TRANSACTIONS_FILE, true);
            double amount = transaction.getAmount();
            double balanceAfter = transaction.getBalanceAfter();
            LocalDateTime dateTime = transaction.getTimestamp();
            String type = transaction.getType();
            String accountNumber = transaction.getAccountNumber();

            writer.write(accountNumber + "|" + type + "|" + amount + "|" +
                    balanceAfter + "|" + dateTime + "\n");
            writer.close();

            System.out.println("Transaction appended successfully.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
