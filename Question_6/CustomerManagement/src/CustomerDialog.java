import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDialog extends JDialog {
    private JTextField shortNameField;
    private JTextField fullNameField;
    private JTextField address1Field;
    private JTextField address2Field;
    private JTextField address3Field;
    private JTextField cityField;
    private JTextField postalCodeField;
    private int customerId = -1;  // Default to -1 for new customers

    // Constructor for adding a new customer
    public CustomerDialog(Frame owner) {
        this(owner, -1);
    }

    // Constructor for modifying an existing customer
    public CustomerDialog(Frame owner, int customerId) {
        super(owner, "Add/Modify Customer", true);
        this.customerId = customerId;

        setLayout(new GridLayout(8, 2, 10, 10));

        // Labels and text fields
        add(new JLabel("Short Name:"));
        shortNameField = new JTextField();
        add(shortNameField);

        add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        add(fullNameField);

        add(new JLabel("Address 1:"));
        address1Field = new JTextField();
        add(address1Field);

        add(new JLabel("Address 2:"));
        address2Field = new JTextField();
        add(address2Field);

        add(new JLabel("Address 3:"));
        address3Field = new JTextField();
        add(address3Field);

        add(new JLabel("City:"));
        cityField = new JTextField();
        add(cityField);

        add(new JLabel("Postal Code:"));
        postalCodeField = new JTextField();
        add(postalCodeField);

        // Buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        add(saveButton);
        add(cancelButton);

        // Load customer data if modifying
        if (customerId != -1) {
            loadCustomerData(customerId);
        }

        // Button actions
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCustomer();
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
    }

    private void loadCustomerData(int customerId) {
        String query = "SELECT * FROM Customers WHERE CustomerID = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    shortNameField.setText(rs.getString("ShortName"));
                    fullNameField.setText(rs.getString("FullName"));
                    address1Field.setText(rs.getString("Address1"));
                    address2Field.setText(rs.getString("Address2"));
                    address3Field.setText(rs.getString("Address3"));
                    cityField.setText(rs.getString("City"));
                    postalCodeField.setText(rs.getString("PostalCode"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveCustomer() {
        String shortName = shortNameField.getText();
        String fullName = fullNameField.getText();
        String address1 = address1Field.getText();
        String address2 = address2Field.getText();
        String address3 = address3Field.getText();
        String city = cityField.getText();
        String postalCode = postalCodeField.getText();

        if (postalCode.isEmpty() || !postalCode.matches("\\d{5}")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid postal code.");
            return;
        }

        String query;
        if (customerId == -1) {  // New customer
            query = "INSERT INTO Customers (ShortName, FullName, Address1, Address2, Address3, City, PostalCode) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {  // Modify existing customer
            query = "UPDATE Customers SET ShortName = ?, FullName = ?, Address1 = ?, Address2 = ?, Address3 = ?, City = ?, PostalCode = ? WHERE CustomerID = ?";
        }

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, shortName);
            pstmt.setString(2, fullName);
            pstmt.setString(3, address1);
            pstmt.setString(4, address2);
            pstmt.setString(5, address3);
            pstmt.setString(6, city);
            pstmt.setString(7, postalCode);

            if (customerId != -1) {  // Modify existing customer
                pstmt.setInt(8, customerId);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
