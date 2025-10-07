import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Method to validate login credentials against database
    public boolean isValid() {
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT * FROM customers WHERE customer_id = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if user found
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Static method to display login error
    public static void displayLoginError() {
        System.out.println("Invalid username or password.");
    }

    // Example usage method
    public static boolean authenticateUser(String username, String password) {
        Login userLogin = new Login(username, password);
        
        if (userLogin.isValid()) {
            // Login successful
            System.out.println("Login successful!");
            return true;
        } else {
            displayLoginError();
            return false;
        }
    }
}