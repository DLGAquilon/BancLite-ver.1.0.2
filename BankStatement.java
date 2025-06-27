import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class BankStatement{
    JFrame statementFrame;
    JTextField[] acctFields = new JTextField[9];
    JButton btnEdit, btnDelete, btnSave, btnBack, btnConfirm;
    JTable transactHistoryTable;
    String loginUser;
    String loginPass;
    JPanel infoPanel, mainPanel, infoButtonPanel, bottomPanel, centerPanel, filterPanel;
    JComboBox<String> filterCombo;
    private final Font helvetica = new Font("Helvetica", Font.PLAIN, 20);
    private final Font monospace = new Font("Monospaced", Font.PLAIN, 14);

    public BankStatement(String username, String password) {
        loginUser = username;
        loginPass = password;

        statementFrame = new JFrame("BancLite - Bank Statement");
        statementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        statementFrame.setSize(1024, 600);
        statementFrame.setLayout(new BorderLayout());
        statementFrame.setResizable(false);
        statementFrame.setLocationRelativeTo(null);
        statementFrame.setBackground(new Color(135, 206, 235));
        statementFrame.setFont(helvetica);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(135, 206, 235));

        infoPanel = new JPanel();
        infoPanel.setBackground(new Color(135, 206, 235));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));

        String[] labels = {"Name", "Age", "Birthday", "Address", "Gender", "Nationality", "Username", "Account No.", "Balance"};
        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel(labels[i] + ":");
            label.setFont(helvetica.deriveFont(Font.BOLD, 16));
            label.setBackground(new Color(135, 206, 235));
            acctFields[i] = new JTextField(15);
            acctFields[i].setEditable(false);
            acctFields[i].setFont(monospace);
            row.setBackground(new Color(135, 206, 235));
            row.add(label);
            row.add(acctFields[i]);
            infoPanel.add(row);
        }

        infoButtonPanel = new JPanel();
        infoButtonPanel.setFont(helvetica);
        infoButtonPanel.setBackground(new Color(135, 206, 235));
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Deactivate");
        btnSave = new JButton("Save");
        btnBack = new JButton("Back");
        btnSave.setVisible(false);
        btnBack.setVisible(false);

        infoButtonPanel.add(btnEdit);
        infoButtonPanel.add(btnSave);
        infoButtonPanel.add(btnBack);
        infoButtonPanel.add(btnDelete);

        infoPanel.add(infoButtonPanel);

        // Load user info
        loadUserInfo();

        // Transaction Table
        String[] columns = {"Transaction", "Amount (PHP)", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        transactHistoryTable = new JTable(model);
        transactHistoryTable.setFont(monospace);
        JScrollPane tableScrollPane = new JScrollPane(transactHistoryTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Transaction History"));
        tableScrollPane.setBackground(new Color(135, 206, 235));

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(135, 206, 235));

        String[] transactTypes = {"Deposit", "Withdraw", "Bank Transfer", "All"};
        filterCombo = new JComboBox<>(transactTypes);
        filterCombo.setFont(new Font("Helvetica", Font.PLAIN, 16));

        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(135, 206, 235));
        filterPanel.add(new JLabel("Filter by: "));
        filterPanel.add(filterCombo);

        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(135, 206, 235));
        btnConfirm = new JButton("Confirm");
        btnConfirm.setFont(helvetica);
        bottomPanel.add(btnConfirm);

        mainPanel.add(infoPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        statementFrame.add(mainPanel);
        statementFrame.setVisible(true);

        loadTransactionHistory(model, "All");

        btnEdit.addActionListener(e -> enableEditing(true));
        btnBack.addActionListener(e -> enableEditing(false));
        btnSave.addActionListener(e -> {
            updateAccountInfo();
            enableEditing(false);
        });
        btnDelete.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(statementFrame, "Deactivate this account?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                deleteAccount();
                statementFrame.dispose();
                new CredentialListing(); // login screen
            }
        });
        btnConfirm.addActionListener(e -> {
            statementFrame.dispose();
            new BancLiteInterface(loginUser, loginPass);
        });
        filterCombo.addActionListener(e -> {
            String selectedType = (String) filterCombo.getSelectedItem();
            loadTransactionHistory(model, selectedType);
        });
    }

    private void enableEditing(boolean editing) {
        for (int i = 0; i < acctFields.length; i++ ) {
            acctFields[i].setEditable(editing);
        }
        acctFields[7].setEditable(false);
        acctFields[8].setEditable(false);
        btnEdit.setVisible(!editing);
        btnSave.setVisible(editing);
        btnBack.setVisible(editing);
    }

    private void loadUserInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("AcctDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[6].trim().equals(loginUser.trim())) {
                    for (int i = 0; i < acctFields.length; i++) {
                        int idx = i < 7 ? i : (i == 7 ? 8 : 10);
                        acctFields[i].setText(parts[idx].trim());
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAccountInfo() {
        try {
            File inputFile = new File("AcctDatabase.txt");
            File tempFile = new File("acct_database_temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[6].trim().equals(loginUser.trim())) {
                    for (int i = 0; i < 7; i++) {
                        parts[i] = acctFields[i].getText().trim();
                    }
                    parts[8] = acctFields[7].getText().trim();
                    parts[10] = acctFields[8].getText().trim();
                    line = String.join(",", parts);
                }
                bw.write(line);
                bw.newLine();
            }

            br.close();
            bw.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
            JOptionPane.showMessageDialog(statementFrame, "Account updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(statementFrame, "Failed to update account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAccount() {
        File inputFile = new File("AcctDatabase.txt");
        File tempFile = new File("acct_database_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");

                if (parts[6].equals(loginUser)) {
                    parts[9] = "Inactive";
                    line = String.join(", ", parts);
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       if (inputFile.delete() && tempFile.renameTo(inputFile)) {
           JOptionPane.showMessageDialog(statementFrame, "Account deactivated. Thank you for using BancLite.");
       } else {
           JOptionPane.showMessageDialog(statementFrame, "Failed to deactivate account.", "Error", JOptionPane.ERROR_MESSAGE);
       }
    }

    private void loadTransactionHistory(DefaultTableModel model, String filterType) {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("transaction_history.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",\\s*");
                if (data[0].trim().equals(loginUser.trim())) {
                    if (filterType.equals("All") || data[1].trim().equals(filterType)) {
                        model.addRow(new Object[]{data[1], data[2], data[3]});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

