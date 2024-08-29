import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainFrame extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public MainFrame() {
        setTitle("Customer Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup table
        String[] columnNames = {"Customer ID", "Short Name", "Full Name", "City"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        // Setup buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Customer");
        JButton modifyButton = new JButton("Modify Customer");
        JButton deleteButton = new JButton("Delete Customer");

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        // Layout
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        addButton.addActionListener(e -> {
            CustomerDialog dialog = new CustomerDialog(this);
            dialog.setVisible(true);
            loadCustomerData();  // Refresh the table after adding a new customer
        });

        modifyButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                int customerId = (int) tableModel.getValueAt(selectedRow, 0);
                CustomerDialog dialog = new CustomerDialog(this, customerId);
                dialog.setVisible(true);
                loadCustomerData();  // Refresh the table after modifying the customer
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to modify.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                int customerId = (int) tableModel.getValueAt(selectedRow, 0);
                deleteCustomer(customerId);
                loadCustomerData();  // Refresh the table after deleting the customer
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
            }
        });

        loadCustomerData();  // Load initial data
        setVisible(true);
    }

    // Method to load customer data from the database into the table
    private void loadCustomerData() {
        tableModel.setRowCount(0);  // Clear existing data
        String query = "SELECT CustomerID, ShortName, FullName, City FROM Customers";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("CustomerID"),
                        rs.getString("ShortName"),
                        rs.getString("FullName"),
                        rs.getString("City")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a customer by ID
    private void deleteCustomer(int customerId) {
        String query = "DELETE FROM Customers WHERE CustomerID = ?";
        try (Connection conn = DatabaseConnection.connect();
             var pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Initialize the database (create tables if they don't exist)
        DatabaseConnection.initialize();

        // Launch the application
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
