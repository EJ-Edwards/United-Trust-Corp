import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseViewer {
    public static void main(String[] args) {
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            
            System.out.println("=== UNITED TRUST CORP DATABASE ===\n");
            
            // View Customers
            System.out.println("CUSTOMERS:");
            System.out.println("----------");
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM customers");
            while (rs1.next()) {
                System.out.printf("ID: %s | Name: %s %s | Email: %s | Phone: %s%n",
                    rs1.getString("customer_id"),
                    rs1.getString("first_name"),
                    rs1.getString("last_name"),
                    rs1.getString("email"),
                    rs1.getString("phone"));
            }
            
            System.out.println("\nACCOUNTS:");
            System.out.println("---------");
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM accounts");
            while (rs2.next()) {
                System.out.printf("Account: %s | Customer: %s | Holder: %s | Balance: $%.2f%n",
                    rs2.getString("account_number"),
                    rs2.getString("customer_id"),
                    rs2.getString("account_holder_name"),
                    rs2.getDouble("balance"));
            }
            
            System.out.println("\nTRANSACTIONS:");
            System.out.println("-------------");
            ResultSet rs3 = stmt.executeQuery("SELECT * FROM transactions ORDER BY transaction_date DESC LIMIT 10");
            while (rs3.next()) {
                System.out.printf("ID: %d | Account: %s | Type: %s | Amount: $%.2f | Date: %s%n",
                    rs3.getInt("transaction_id"),
                    rs3.getString("account_number"),
                    rs3.getString("transaction_type"),
                    rs3.getDouble("amount"),
                    rs3.getString("transaction_date"));
            }
            
            Database.closeConnection();
            System.out.println("\n=== END OF DATABASE VIEW ===");
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}