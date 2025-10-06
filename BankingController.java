import java.util.ArrayList;
import java.util.List;

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
        // Check if customer ID already exists
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return false; // Customer already exists
            }
        }
        
        Customer newCustomer = new Customer(customerId, firstName, lastName, email, phone);
        customers.add(newCustomer);
        return true;
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

        BankAccount newAccount = new BankAccount(accountNumber, accountHolderName, initialBalance);
        currentCustomer.addAccount(newAccount);
        currentAccount = newAccount; // Set as active account
        return true;
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
}