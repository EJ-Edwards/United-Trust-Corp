public class BankAccount {
    private String accountnumber;
    private String accountHolderName;
    private double balance;

    public BankAccount(String accountnumber, String accountHolderName, double initialBalance) {
        this.accountnumber = accountnumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew: " + amount);
        } else {
            System.out.println("Insufficient Funds");
        }
    }

    
    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountnumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        // Get account details from user
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        System.out.print("Enter account holder name: ");
        String holderName = scanner.nextLine();
        
        System.out.print("Enter initial balance: $");
        double initialBalance = scanner.nextDouble();
        
        // Create the bank account
        BankAccount account = new BankAccount(accountNumber, holderName, initialBalance);
        
        System.out.println("\n=== Welcome to United Trust Corp Banking System ===");
        System.out.println("Account created successfully!");
        System.out.println("Account Holder: " + account.getAccountHolderName());
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Current Balance: $" + account.getBalance());
        
        // Main menu loop
        while (true) {
            System.out.println("\n=== Banking Menu ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Choose an option (1-4): ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.println("Current Balance: $" + account.getBalance());
                    break;
                    
                case 2:
                    System.out.print("Enter deposit amount: $");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    System.out.println("New Balance: $" + account.getBalance());
                    break;
                    
                case 3:
                    System.out.print("Enter withdrawal amount: $");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    System.out.println("Current Balance: $" + account.getBalance());
                    break;
                    
                case 4:
                    System.out.println("Thank you for banking with United Trust Corp!");
                    System.out.println("Final Balance: $" + account.getBalance());
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Invalid option. Please choose 1-4.");
            }
        }
    }
}