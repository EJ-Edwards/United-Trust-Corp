import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * VIEW CLASS - Welcome page GUI
 * This is the entry point for your banking application
 */
public class WelcomePage extends JFrame {
    private BankingController controller;
    private JTextField customerIdField;

    public WelcomePage() {
        controller = new BankingController();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("United Trust Corp - Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create main content
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Create footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 102, 204));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel titleLabel = new JLabel("United Trust Corp", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Banking System", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);

        panel.setLayout(new BorderLayout());
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();

        // Customer ID input
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(new JLabel("Customer ID:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        customerIdField = new JTextField(15);
        customerIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(customerIdField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 10, 10, 10);
        
        JButton loginButton = createStyledButton("Login", new Color(0, 150, 0));
        loginButton.addActionListener(new LoginListener());
        panel.add(loginButton, gbc);

        gbc.gridy = 2;
        JButton createAccountButton = createStyledButton("Create New Customer", new Color(0, 102, 204));
        createAccountButton.addActionListener(new CreateCustomerListener());
        panel.add(createAccountButton, gbc);

        gbc.gridy = 3;
        JButton exitButton = createStyledButton("Exit", new Color(200, 0, 0));
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton, gbc);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel footerLabel = new JLabel("© 2025 United Trust Corp. All rights reserved.", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        panel.add(footerLabel);
        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }

    // Event Listeners
    private class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String customerId = customerIdField.getText().trim();
            
            if (customerId.isEmpty()) {
                JOptionPane.showMessageDialog(WelcomePage.this, "Please enter a Customer ID");
                return;
            }

            if (controller.loginCustomer(customerId)) {
                // Open main banking window
                new MainBankingWindow(controller).setVisible(true);
                dispose(); // Close welcome page
            } else {
                JOptionPane.showMessageDialog(WelcomePage.this, 
                    "Customer ID not found. Please create a new customer account.");
            }
        }
    }

    private class CreateCustomerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Open customer creation dialog
            new CreateCustomerDialog(WelcomePage.this, controller).setVisible(true);
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            // Set look and feel to system default for better appearance
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new WelcomePage().setVisible(true);
        });
    }
}

    
