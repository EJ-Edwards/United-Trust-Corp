import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        setSize(600, 700); // Increased size to accommodate logo and all components
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Try to load logo image
        try {
            ImageIcon originalIcon = new ImageIcon("resources/images/ChatGPT Image Oct 5, 2025, 10_32_15 PM.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Better proportions for square logo
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledIcon);
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
            
            // Add logo to header panel
            JPanel headerWithLogo = createHeaderPanel();
            headerWithLogo.add(logoLabel, BorderLayout.NORTH);
            add(headerWithLogo, BorderLayout.NORTH);
        } catch (Exception e) {
            // If image fails to load, just use the regular header
            System.out.println("Could not load logo image: " + e.getMessage());
            JPanel headerPanel = createHeaderPanel();
            add(headerPanel, BorderLayout.NORTH);
        }

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
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 25, 20)); // Adjusted padding

        JLabel titleLabel = new JLabel("United Trust Corp", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Slightly smaller to fit better
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Banking System", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);

        panel.setLayout(new BorderLayout());
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Better spacing for larger window
        GridBagConstraints gbc = new GridBagConstraints();

        // Customer ID input
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(15, 15, 15, 15); // More spacing between elements
        gbc.anchor = GridBagConstraints.EAST;
        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(idLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        customerIdField = new JTextField(20); // Wider text field
        customerIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        customerIdField.setPreferredSize(new Dimension(200, 30));
        panel.add(customerIdField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(25, 15, 15, 15);
        
        JButton loginButton = createStyledButton("Login", new Color(0, 150, 0));
        loginButton.addActionListener(new LoginListener());
        panel.add(loginButton, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(15, 15, 15, 15);
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
        JLabel footerLabel = new JLabel("2025 United Trust Corp. All rights reserved.", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        panel.add(footerLabel);
        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font for bigger window
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(250, 45)); // Larger buttons
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