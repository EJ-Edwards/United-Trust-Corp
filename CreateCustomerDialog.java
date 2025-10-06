import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * VIEW CLASS - Dialog for creating new customers
 */
public class CreateCustomerDialog extends JDialog {
    private BankingController controller;
    private JTextField customerIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;

    public CreateCustomerDialog(JFrame parent, BankingController controller) {
        super(parent, "Create New Customer", true);
        this.controller = controller;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(400, 350);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Customer ID
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Customer ID:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        customerIdField = new JTextField(15);
        formPanel.add(customerIdField, gbc);

        // First Name
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        firstNameField = new JTextField(15);
        formPanel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        lastNameField = new JTextField(15);
        formPanel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create Customer");
        createButton.addActionListener(new CreateListener());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class CreateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String customerId = customerIdField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            // Validate input
            if (customerId.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(CreateCustomerDialog.this,
                    "Customer ID, First Name, and Last Name are required");
                return;
            }

            // Create customer
            if (controller.createCustomer(customerId, firstName, lastName, email, phone)) {
                JOptionPane.showMessageDialog(CreateCustomerDialog.this,
                    "Customer created successfully! You can now login with ID: " + customerId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(CreateCustomerDialog.this,
                    "Customer ID already exists. Please choose a different ID.");
            }
        }
    }
}