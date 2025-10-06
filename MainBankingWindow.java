import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * VIEW CLASS - Main banking operations window
 */
public class MainBankingWindow extends JFrame {
    private BankingController controller;
    private JLabel customerLabel;
    private JLabel accountLabel;
    private JLabel balanceLabel;
    private JTextField amountField;
    private JTextField accountField;
    private JTextArea transactionArea;
    private JComboBox<String> accountComboBox;

    public MainBankingWindow(BankingController controller) {
        this.controller = controller;
        initializeGUI();
        updateDisplay();
    }

    private void initializeGUI() {
        setTitle("United Trust Corp - Banking Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createTransactionPanel(), BorderLayout.SOUTH);

        // Create menu bar
        setJMenuBar(createMenuBar());
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 102, 204));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        customerLabel = new JLabel("Customer: " + controller.getCurrentCustomer().getFullName());
        customerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        customerLabel.setForeground(Color.WHITE);

        accountLabel = new JLabel("Account: Not selected");
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        accountLabel.setForeground(Color.WHITE);

        balanceLabel = new JLabel("Balance: $0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(Color.YELLOW);

        panel.add(customerLabel, BorderLayout.NORTH);
        panel.add(accountLabel, BorderLayout.CENTER);
        panel.add(balanceLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // Account selection
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(new JLabel("Select Account:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        accountComboBox = new JComboBox<>();
        accountComboBox.addActionListener(e -> selectAccount());
        panel.add(accountComboBox, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        JButton createAccountBtn = new JButton("Create New Account");
        createAccountBtn.addActionListener(e -> createNewAccount());
        panel.add(createAccountBtn, gbc);

        // Amount input
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Amount: $"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        amountField = new JTextField(15);
        panel.add(amountField, gbc);

        // Banking operation buttons
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton depositBtn = createStyledButton("Deposit", new Color(0, 150, 0));
        depositBtn.addActionListener(e -> deposit());
        panel.add(depositBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton withdrawBtn = createStyledButton("Withdraw", new Color(200, 0, 0));
        withdrawBtn.addActionListener(e -> withdraw());
        panel.add(withdrawBtn, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        JButton balanceBtn = createStyledButton("Check Balance", new Color(0, 102, 204));
        balanceBtn.addActionListener(e -> checkBalance());
        panel.add(balanceBtn, gbc);

        // Transfer section
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(new JLabel("Transfer to Account:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        accountField = new JTextField(15);
        panel.add(accountField, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        JButton transferBtn = createStyledButton("Transfer", new Color(150, 0, 150));
        transferBtn.addActionListener(e -> transfer());
        panel.add(transferBtn, gbc);

        return panel;
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Transaction History"));

        transactionArea = new JTextArea(8, 80);
        transactionArea.setEditable(false);
        transactionArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        transactionArea.setBackground(new Color(248, 248, 248));

        JScrollPane scrollPane = new JScrollPane(transactionArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        return menuBar;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    private void updateDisplay() {
        // Update account combo box
        accountComboBox.removeAllItems();
        List<BankAccount> accounts = controller.getCurrentCustomerAccounts();
        for (BankAccount account : accounts) {
            accountComboBox.addItem(account.getAccountNumber() + " - " + account.getAccountHolderName());
        }

        // Update current account display
        BankAccount currentAccount = controller.getCurrentAccount();
        if (currentAccount != null) {
            accountLabel.setText("Account: " + currentAccount.getAccountNumber());
            balanceLabel.setText("Balance: $" + String.format("%.2f", currentAccount.getBalance()));
            updateTransactionHistory();
        } else {
            accountLabel.setText("Account: Not selected");
            balanceLabel.setText("Balance: $0.00");
            transactionArea.setText("Please select an account to view transactions.");
        }
    }

    private void updateTransactionHistory() {
        BankAccount currentAccount = controller.getCurrentAccount();
        if (currentAccount != null) {
            StringBuilder sb = new StringBuilder();
            List<Transaction> transactions = currentAccount.getTransactionHistory();
            for (Transaction transaction : transactions) {
                sb.append(transaction.toString()).append("\n");
            }
            transactionArea.setText(sb.toString());
            transactionArea.setCaretPosition(transactionArea.getDocument().getLength());
        }
    }

    // Event handlers
    private void selectAccount() {
        if (accountComboBox.getSelectedItem() != null) {
            String selected = (String) accountComboBox.getSelectedItem();
            String accountNumber = selected.split(" - ")[0];
            controller.selectAccount(accountNumber);
            updateDisplay();
        }
    }

    private void deposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String result = controller.deposit(amount);
            JOptionPane.showMessageDialog(this, result);
            amountField.setText("");
            updateDisplay();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount");
        }
    }

    private void withdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String result = controller.withdraw(amount);
            JOptionPane.showMessageDialog(this, result);
            amountField.setText("");
            updateDisplay();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount");
        }
    }

    private void transfer() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String recipientAccount = accountField.getText().trim();
            
            if (recipientAccount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter recipient account number");
                return;
            }
            
            String result = controller.transfer(recipientAccount, amount);
            JOptionPane.showMessageDialog(this, result);
            amountField.setText("");
            accountField.setText("");
            updateDisplay();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount");
        }
    }

    private void checkBalance() {
        BankAccount currentAccount = controller.getCurrentAccount();
        if (currentAccount != null) {
            JOptionPane.showMessageDialog(this, 
                "Current Balance: $" + String.format("%.2f", currentAccount.getBalance()));
        } else {
            JOptionPane.showMessageDialog(this, "Please select an account first");
        }
    }

    private void createNewAccount() {
        new CreateAccountDialog(this, controller).setVisible(true);
        updateDisplay(); // Refresh after account creation
    }

    private void logout() {
        controller.logout();
        new WelcomePage().setVisible(true);
        dispose();
    }
}