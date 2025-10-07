import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CONTROLLER CLASS - Handles business logic and coordinates between Model and View
 */
public class BankingController {
    private List<Customer> customers;
    private Customer currentCustomer;
    private BankAccount currentAccount;

    public BankingController() {
        this.customers = new ArrayList<>();
    }

    // Customer management
    public boolean createCustomer(String customerId, String firstName, String lastName, String email, String phone) {
        // Check if customer ID already exists in database
        if (customerExistsInDatabase(customerId)) {
            return false; // Customer already exists
        }
        
        // Save to database
        if (saveCustomerToDatabase(customerId, firstName, lastName, email, phone)) {
            // Also keep in memory for current session
            Customer newCustomer = new Customer(customerId, firstName, lastName, email, phone);
            customers.add(newCustomer);
            return true;
        }
        return false;
    }

    public boolean loginCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                currentCustomer = customer;
                return true;
            }
        }
        return false;
    }

    // Account management
    public boolean createAccount(String accountNumber, String accountHolderName, double initialBalance) {
        if (currentCustomer == null) {
            return false;
        }

        // Check if account number already exists
        if (findAccountByNumber(accountNumber) != null) {
            return false; // Account already exists
        }

        // Save to database
        if (saveAccountToDatabase(accountNumber, currentCustomer.getCustomerId(), accountHolderName, initialBalance)) {
            // Also keep in memory for current session
            BankAccount newAccount = new BankAccount(accountNumber, accountHolderName, initialBalance);
            currentCustomer.addAccount(newAccount);
            currentAccount = newAccount; // Set as active account
            
            // Save initial balance as a transaction
            saveTransactionToDatabase(accountNumber, "INITIAL_DEPOSIT", initialBalance, "Account creation");
            return true;
        }
        return false;
    }

    public boolean selectAccount(String accountNumber) {
        if (currentCustomer == null) {
            return false;
        }

        BankAccount account = currentCustomer.getAccount(accountNumber);
        if (account != null) {
            currentAccount = account;
            return true;
        }
        return false;
    }

    // Banking operations
    public String deposit(double amount) {
        if (currentAccount == null) {
            return "No account selected";
        }

        if (currentAccount.deposit(amount)) {
            // Save transaction to database
            saveTransactionToDatabase(currentAccount.getAccountNumber(), "DEPOSIT", amount, "Deposit transaction");
            return "Successfully deposited $" + String.format("%.2f", amount);
        } else {
            return "Invalid deposit amount";
        }
    }

    public String withdraw(double amount) {
        if (currentAccount == null) {
            return "No account selected";
        }

        if (currentAccount.withdraw(amount)) {
            // Save transaction to database
            saveTransactionToDatabase(currentAccount.getAccountNumber(), "WITHDRAWAL", amount, "Withdrawal transaction");
            return "Successfully withdrew $" + String.format("%.2f", amount);
        } else {
            return "Insufficient funds or invalid amount";
        }
    }

    public String transfer(String recipientAccountNumber, double amount) {
        if (currentAccount == null) {
            return "No account selected";
        }

        BankAccount recipientAccount = findAccountByNumber(recipientAccountNumber);
        if (recipientAccount == null) {
            return "Recipient account not found";
        }

        if (currentAccount.internalTransfer(recipientAccountNumber, amount)) {
            // Add money to recipient account
            recipientAccount.deposit(amount);
            
            // Save both transactions to database
            saveTransactionToDatabase(currentAccount.getAccountNumber(), "TRANSFER_OUT", amount, "Transfer to " + recipientAccountNumber);
            saveTransactionToDatabase(recipientAccountNumber, "TRANSFER_IN", amount, "Transfer from " + currentAccount.getAccountNumber());
            
            return "Successfully transferred $" + String.format("%.2f", amount) + " to " + recipientAccountNumber;
        } else {
            return "Transfer failed - insufficient funds or invalid amount";
        }
    }

    // Helper methods
    private BankAccount findAccountByNumber(String accountNumber) {
        for (Customer customer : customers) {
            BankAccount account = customer.getAccount(accountNumber);
            if (account != null) {
                return account;
            }
        }
        return null;
    }

    // Getters for current state
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public BankAccount getCurrentAccount() {
        return currentAccount;
    }

    public List<BankAccount> getCurrentCustomerAccounts() {
        if (currentCustomer != null) {
            return currentCustomer.getAllAccounts();
        }
        return new ArrayList<>();
    }

    public void logout() {
        currentCustomer = null;
        currentAccount = null;
    }
    
    // Database helper methods
    private boolean customerExistsInDatabase(String customerId) {
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT customer_id FROM customers WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if customer exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean saveCustomerToDatabase(String customerId, String firstName, String lastName, String email, String phone) {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean saveAccountToDatabase(String accountNumber, String customerId, String accountHolderName, double balance) {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO accounts (account_number, customer_id, account_holder_name, balance) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            stmt.setString(2, customerId);
            stmt.setString(3, accountHolderName);
            stmt.setDouble(4, balance);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void saveTransactionToDatabase(String accountNumber, String transactionType, double amount, String description) {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO transactions (account_number, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            stmt.setString(2, transactionType);
            stmt.setDouble(3, amount);
            stmt.setString(4, description);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}