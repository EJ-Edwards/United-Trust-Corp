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