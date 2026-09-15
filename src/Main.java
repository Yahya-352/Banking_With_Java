import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        System.out.println("3. Back");
        boolean running = true;
        while (running){
            System.out.println("please enter a number to choose the account you want to transfer from:");
            int accType = sc.nextInt();
            if(accType == 1){
                System.out.println("enter the account you want to transfer to:");
                String AccNumber = sc.next();
                System.out.println("enter the amount you want to transfer:");
                double amount = sc.nextDouble();
                if(customer.getCheckingAccount() != null){
                    try{
                        bank.transfer(customer.getCheckingAccount(), AccNumber , amount);
                    }catch (Exception e){
                        System.out.println("transfer failed: " + e.getMessage());
                    }
                }else{
                    System.out.println("you dont have a checking account");
                }
                running = false;
            }else if(accType ==2){
                System.out.println("enter the account you want to transfer to:");
                String AccNumber = sc.next();
                System.out.println("enter the amount you want to transfer:");
                double amount = sc.nextDouble();
                if(customer.getSavingsAccount() != null){
                    try{
                        bank.transfer(customer.getSavingsAccount(), AccNumber , amount);
                    }catch (Exception e){
                        System.out.println("transfer failed: " + e.getMessage());
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
    private static void transactionHistory(){
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
        System.out.println("3. Back");
        System.out.println("Enter the account type you want to view your transaction history for:");
        int acctype = sc.nextInt();
        if(acctype == 1){
            if(customer.getCheckingAccount() != null){
                showFilteredHistory(customer.getCheckingAccount());
            }else{
                System.out.println("you dont have a checking account!");
            }
            }else if (acctype == 2){
                if(customer.getSavingsAccount() != null){
                    showFilteredHistory(customer.getSavingsAccount());
                }else{
                    System.out.println("you dont have a savings account!");
                }
            }else if(acctype == 3) {
                customerMenuPage();
            }
        customerMenuPage();
    }
    private static void showFilteredHistory(Account account){
        List<Transaction> allTransactions = bank.getTransactionHistory(account);
        System.out.println("1. All");
        System.out.println("2. Today");
        System.out.println("3. Yesterday");
        System.out.println("4. Last 7 Days");
        System.out.println("5. Last 30 Days");
        System.out.println("6. Filter by month number");
        System.out.println("7. Choose start Date and End date");
        System.out.println("Choose a filter:");
        int chosenFilter = sc.nextInt();
        List<Transaction> filteredTransactions;
        if(chosenFilter == 1){
            filteredTransactions = allTransactions;
        }else if(chosenFilter == 2){
            filteredTransactions = bank.getTransactionsForToday(allTransactions);
        }else if(chosenFilter == 3){
            filteredTransactions = bank.getTransactionsForYesterday(allTransactions);
        }else if(chosenFilter == 4){
            filteredTransactions = bank.getTransactionsForLast7Days(allTransactions);
        }else if(chosenFilter == 5){
            filteredTransactions = bank.getTransactionsForLast30Days(allTransactions);
        }else if(chosenFilter == 6){
            System.out.println("Enter month number");
            int month = sc.nextInt();
            filteredTransactions = bank.getTransactionsByMonth(allTransactions,month);
        }else if(chosenFilter == 7){
            try{
                System.out.println("Enter Start Date");
                LocalDate startDate = LocalDate.parse(sc.next());
                System.out.println("Enter End Date");
                LocalDate endDate = LocalDate.parse(sc.next());
                filteredTransactions = bank.getTransactionsForCustomDates(allTransactions ,startDate , endDate );
            }catch (Exception e){
                System.out.println("Invalid date format .. printing all transactions");
                filteredTransactions = allTransactions;
            }
        }
        else{
            filteredTransactions = allTransactions;
        }
        for(int i = 0 ; i < filteredTransactions.size() ; i++){
            System.out.println(filteredTransactions.get(i));
        }
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
        customerMenuPage();
    }

    private static void bankerPage(){

    }
}