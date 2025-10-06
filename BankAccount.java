import java.util.ArrayList;
import java.util.List;

/**
 * MODEL CLASS - Pure data and business rules
 * No GUI code, no System.out.println
 */
public class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private List<Transaction> transactionHistory;
    private boolean internalTransferEnabled;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        this.internalTransferEnabled = true;
        
        // Record initial deposit as first transaction
        if (initialBalance > 0) {
            transactionHistory.add(new Transaction("INITIAL_DEPOSIT", initialBalance, balance));
        }
    }

    // Returns true if deposit successful, false otherwise
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add(new Transaction("DEPOSIT", amount, balance));
            return true;
        }
        return false;
    }

    // Returns true if withdrawal successful, false otherwise
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Transaction("WITHDRAWAL", -amount, balance));
            return true;
        }
        return false;
    }

    // Returns true if transfer successful, false otherwise
    public boolean internalTransfer(String recipientAccountNumber, double amount) {
        if (internalTransferEnabled && amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Transaction("TRANSFER_OUT to " + recipientAccountNumber, -amount, balance));
            return true;
        }
        return false;
    }

    // Getters
    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Return copy for security
    }

    public boolean isInternalTransferEnabled() {
        return internalTransferEnabled;
    }

    // Setters
    public void setInternalTransferEnabled(boolean enabled) {
        this.internalTransferEnabled = enabled;
    }

}