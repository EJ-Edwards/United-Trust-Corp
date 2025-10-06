import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * VIEW CLASS - Dialog for creating new accounts
 */
public class CreateAccountDialog extends JDialog {
    private BankingController controller;
    private JTextField accountNumberField;
    private JTextField accountHolderField;
    private JTextField initialBalanceField;

    public CreateAccountDialog(JFrame parent, BankingController controller) {
        super(parent, "Create New Account", true);
        this.controller = controller;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(400, 250);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Account Number
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Account Number:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        accountNumberField = new JTextField(15);
        formPanel.add(accountNumberField, gbc);

        // Account Holder Name
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Account Holder:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        accountHolderField = new JTextField(15);
        // Pre-fill with customer name
        if (controller.getCurrentCustomer() != null) {
            accountHolderField.setText(controller.getCurrentCustomer().getFullName());
        }
        formPanel.add(accountHolderField, gbc);

        // Initial Balance
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Initial Balance:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        initialBalanceField = new JTextField(15);
        initialBalanceField.setText("0.00");
        formPanel.add(initialBalanceField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create Account");
        createButton.addActionListener(new CreateListener());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class CreateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String accountNumber = accountNumberField.getText().trim();
            String accountHolder = accountHolderField.getText().trim();
            String balanceText = initialBalanceField.getText().trim();

            // Validate input
            if (accountNumber.isEmpty() || accountHolder.isEmpty()) {
                JOptionPane.showMessageDialog(CreateAccountDialog.this,
                    "Account Number and Account Holder are required");
                return;
            }

            try {
                double initialBalance = Double.parseDouble(balanceText);
                
                if (initialBalance < 0) {
                    JOptionPane.showMessageDialog(CreateAccountDialog.this,
                        "Initial balance cannot be negative");
                    return;
                }

                // Create account
                if (controller.createAccount(accountNumber, accountHolder, initialBalance)) {
                    JOptionPane.showMessageDialog(CreateAccountDialog.this,
                        "Account created successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(CreateAccountDialog.this,
                        "Account number already exists. Please choose a different number.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CreateAccountDialog.this,
                    "Please enter a valid initial balance");
            }
        }
    }
}