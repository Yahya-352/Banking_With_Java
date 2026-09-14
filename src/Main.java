import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Bank bank = new Bank();
        Scanner sc = new Scanner(System.in);

        welcomePage(bank , sc);

    }

    private static void welcomePage(Bank bank , Scanner sc){
        System.out.println("Welcome to ACME Bank!");
        System.out.println("1. Customer");
        System.out.println("2. Banker");
        boolean running = true;
        while (running){
            System.out.println("please enter a number to select your role:");
            int role = sc.nextInt();
            if(role == 1){
                customerLoginPage(bank , sc);
                running = false;
            }else if(role ==2){
                bankerPage();
                running = false;
            }else{
                System.out.println("please enter 1 for Customer or 2 for Banker");
            }
        }

    }

    private static void customerLoginPage(Bank bank , Scanner sc){
        System.out.println("Enter your username");
        String username = sc.next();
        System.out.println("enter your password");
        String password = sc.next();
        try{
            Customer customer = (Customer) bank.login(username , password);
            customerMenuPage(customer , sc);
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            welcomePage(bank , sc);
        }
    }

    private static void customerMenuPage(Customer customer , Scanner sc ){
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. Transaction History");
        System.out.println("5. Account Statement");
        System.out.println("6. My accounts");

        int choice = sc.nextInt();
        if(choice == 1){
            deposit();
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
    private static void deposit(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
    }
    private static void WithDraw(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
    }
    private static void Transfer(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
    }
    private static void transactionHistory(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
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
            System.out.println("DFgfgd");
        }
    }

    private static void bankerPage(){

    }
}