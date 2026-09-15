import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Bank bank;
    public static void main(String[] args) throws IOException {
        bank = new Bank();
        welcomePage(bank);

    }
    static Scanner sc = new Scanner(System.in);

    private static void welcomePage(Bank bank){
        System.out.println("Welcome to ACME Bank!");
        System.out.println("1. Customer");
        System.out.println("2. Banker");
        boolean running = true;
        while (running){
            System.out.println("please enter a number to select your role:");
            int role = sc.nextInt();
            if(role == 1){
                customerLoginPage(bank);
                running = false;
            }else if(role ==2){
                bankerPage();
                running = false;
            }else{
                System.out.println("please enter 1 for Customer or 2 for Banker");
            }
        }

    }
    static Customer customer;
    private static void customerLoginPage(Bank bank){
        System.out.println("Enter your username");
        String username = sc.next();
        System.out.println("enter your password");
        String password = sc.next();
        try{
            customer = (Customer) bank.login(username , password);
            customerMenuPage();
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            welcomePage(bank);
        }
    }

    private static void customerMenuPage(){
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. Transaction History");
        System.out.println("5. Account Statement");
        System.out.println("6. My accounts");

        int choice = sc.nextInt();
        if(choice == 1){
            deposit(bank);
        }else if(choice == 2){
            WithDraw();
        }else if(choice == 3){
            Transfer();
        }else if(choice == 4){
            transactionHistory();
        }else if(choice == 5){
            accountStatement();
        }
        if(choice == 6){
            viewAccounts(customer);
        }
    }
    private static void deposit(Bank bank){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
        System.out.println("3. Back");
        boolean running = true;
        while (running){
            System.out.println("please enter a number to choose the account you want to deposit to:");
            int accType = sc.nextInt();
            if(accType == 1){
                System.out.println("enter the amount you want to deposit");
                double amount = sc.nextDouble();
                if(customer.getCheckingAccount() != null){
                    try{
                        bank.deposit(customer.getCheckingAccount() , amount);
                        System.out.println("Deposit successful");
                    }catch (Exception e){
                        System.out.println("Deposit failed: " + e.getMessage());
                    }
                }else{
                    System.out.println("you dont have a checking account");
                }
                running = false;
            }else if(accType ==2){
                System.out.println("enter the amount you want to deposit");
                double amount = sc.nextDouble();
                if(customer.getSavingsAccount() != null){
                    try{
                        bank.deposit(customer.getSavingsAccount() , amount);
                        System.out.println("Deposit Successful");
                    }catch (Exception e){
                        System.out.println("Deposit failed: " + e.getMessage());
                    }
                }else{
                    System.out.println("you dont have a saving account");
                }
                running = false;
            }else if (accType == 3){
                running = false;
            }else{
                System.out.println("please enter a valid option:");
            }
        }
        customerMenuPage();
    }


    private static void WithDraw(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
        System.out.println("3. Back");
        boolean running = true;
        while (running){
            System.out.println("please enter a number to choose the account you want to Withdraw from:");
            int accType = sc.nextInt();
            if(accType == 1){
                System.out.println("enter the amount you want to Withdraw");
                double amount = sc.nextDouble();
                if(customer.getCheckingAccount() != null){
                    try{
                        bank.withdraw(customer.getCheckingAccount() , amount);
                        System.out.println("Withdraw successful");
                    }catch (Exception e){
                        System.out.println("Withdraw failed: " + e.getMessage());
                    }
                }else{
                    System.out.println("you dont have a checking account");
                }
                running = false;
            }else if(accType ==2){
                System.out.println("enter the amount you want to Withdraw");
                double amount = sc.nextDouble();
                if(customer.getSavingsAccount() != null){
                    try{
                        bank.withdraw(customer.getSavingsAccount() , amount);
                        System.out.println("Withdraw Successful");
                    }catch (Exception e){
                        System.out.println("Withdraw failed: " + e.getMessage());
                    }
                }else{
                    System.out.println("you dont have a saving account");
                }
                running = false;
            }else if (accType == 3){
                running = false;
            }else{
                System.out.println("please enter a valid option:");
            }
        }
        customerMenuPage();
    }
    private static void Transfer(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
    }
    private static void transactionHistory(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
        System.out.println("3. Back");
        System.out.println("Enter the account type you want to view your transaction history for:");
        int acctype = sc.nextInt();
        boolean running = true;
        if(acctype == 1){
            if(customer.getCheckingAccount() != null){
                List<Transaction> transactions = bank.getTransactionHistory(customer.getCheckingAccount());
                for(int i = 0 ; i < transactions.size() ; i++){
                    System.out.println(transactions.get(i));
                }
                }else{
                    System.out.println("you dont have a checking account!");
                }
            }else if (acctype == 2){
                if(customer.getSavingsAccount() != null){
                    List<Transaction> transactions = bank.getTransactionHistory(customer.getSavingsAccount());
                    for(int i = 0 ; i < transactions.size() ; i++){
                        System.out.println(transactions.get(i));
                    }
                }else{
                    System.out.println("you dont have a savings account!");
                }
            }else if(acctype == 3) {
                customerMenuPage();
            }
        customerMenuPage();
    }
    private static void accountStatement(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
    }

    private static void viewAccounts(Customer customer){
        ArrayList<Account> accounts = new ArrayList<>();
        if(customer.getCheckingAccount() != null){
            accounts.add(customer.getCheckingAccount());
        }
        if(customer.getSavingsAccount() != null){
            accounts.add(customer.getSavingsAccount());
        }
        for(Account account : accounts){
            System.out.println(account);
        }
    }

    private static void bankerPage(){

    }
}