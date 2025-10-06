import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MODEL CLASS - Represents a single transaction
 */
public class Transaction {
    private String type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String description;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.description = generateDescription();
    }

    private String generateDescription() {
        if (amount >= 0) {
            return type + " of $" + String.format("%.2f", Math.abs(amount));
        } else {
            return type + " of $" + String.format("%.2f", Math.abs(amount));
        }
    }

    // Getters
    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Balance: $%.2f", 
                           getFormattedTimestamp(), description, balanceAfter);
    }
}