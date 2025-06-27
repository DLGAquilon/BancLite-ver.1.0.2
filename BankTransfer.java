import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BankTransfer extends JFrame {
    JLabel lblTitle;
    private JTextField txtAmnt, txtAcctName, txtAcctNum;
    private final Font helvetica = new Font("Helvetica", Font.PLAIN, 24);
    private String currentUser;
    JButton btnTransfer, btnBack;
    JPanel formPanel, btnPanel;

    public BankTransfer(String currentUser) {
        this.currentUser = currentUser;

        setTitle("BancLite - Bank Transfer");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(135, 206, 235));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(135, 206, 235));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        lblTitle = new JLabel("Bank Transfer", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Helvetica", Font.BOLD, 48));
        lblTitle.setBackground(new Color(135, 206, 235));
        lblTitle.setOpaque(true);

        txtAmnt = new JTextField(15);
        txtAmnt.setFont(helvetica);
        txtAcctName = new JTextField(15);
        txtAcctName.setFont(helvetica);
        txtAcctNum = new JTextField(15);
        txtAcctNum.setFont(helvetica);

        formPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        formPanel.setBackground(new Color(135, 206, 235));
        formPanel.setFont(helvetica);
        JLabel lblAmount = new JLabel("Amount:", SwingConstants.LEFT);
        lblAmount.setFont(helvetica);
        lblAmount.setBackground(new Color(135, 206, 235));
        lblAmount.setOpaque(true);
        formPanel.add(lblAmount);
        formPanel.add(txtAmnt);
        JLabel lblRecipient = new JLabel("Recipient Name:", SwingConstants.LEFT);
        lblRecipient.setFont(helvetica);
        lblRecipient.setBackground(new Color(135, 206, 235));
        lblRecipient.setOpaque(true);
        formPanel.add(lblRecipient);
        formPanel.add(txtAcctName);
        JLabel lblAcctNum = new JLabel("Recipient Account Number:", SwingConstants.LEFT);
        lblAcctNum.setFont(helvetica);
        lblAcctNum.setBackground(new Color(135, 206, 235));
        lblAcctNum.setOpaque(true);
        formPanel.add(lblAcctNum);
        formPanel.add(txtAcctNum);

        btnTransfer = new JButton("Send");
        btnTransfer.setFont(helvetica);
        btnTransfer.setPreferredSize(new Dimension(150, 45));
        btnTransfer.setMaximumSize(new Dimension(150, 45));
        btnTransfer.setMinimumSize(new Dimension(150, 45));

        btnBack = new JButton("Back");
        btnBack.setFont(helvetica);
        btnBack.setPreferredSize(new Dimension(150, 45));
        btnBack.setMaximumSize(new Dimension(150, 45));
        btnBack.setMinimumSize(new Dimension(150, 45));

        btnTransfer.addActionListener(e -> handleTransfer());
        btnBack.addActionListener(e -> {
            dispose();
            new BancLiteInterface(currentUser, null);
        });

        btnPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        btnPanel.setBackground(new Color(135, 206, 235));
        btnPanel.setFont(helvetica);
        btnPanel.add(btnTransfer);
        btnPanel.add(btnBack);

        centerPanel.add(lblTitle, gbc);
        gbc.gridy++;
        centerPanel.add(formPanel, gbc);
        gbc.gridy++;
        centerPanel.add(btnPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void handleTransfer() {
        String amountStr = txtAmnt.getText().trim();
        String senderName = txtAcctName.getText().trim();
        String senderAcctNum = txtAcctNum.getText().trim();

        if (amountStr.isEmpty() || senderName.isEmpty() || senderAcctNum.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!senderAcctNum.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Invalid account number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount = 0.00;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please insert a minimum of PHP 1 to send", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        File dbFile = new File("AcctDatabase.txt");
        File tempFile = new File("temp_acct_database.txt");
        boolean senderFound = false;
        boolean currentUserFound = false;
        double senderBalance = 0, currentUserBalance = 0;
        StringBuilder updatedData = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(dbFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\s*,\\s*");
                if (data.length < 11) continue;

                String name = data[0];
                String acctNum = data[8];
                String username = data[6];
                double balance = Double.parseDouble(data[10]);

                if (name.equalsIgnoreCase(senderName) && acctNum.equals(senderAcctNum)) {
                    senderBalance = balance + amount;
                    senderFound = true;
                    line = updateLineWithBalance(data, senderBalance);
                }

                if (username.equals(currentUser)) {
                    if (balance < amount) {
                        JOptionPane.showMessageDialog(this, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    currentUserBalance = balance - amount;
                    currentUserFound = true;
                    line = updateLineWithBalance(data, currentUserBalance);
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "File processing error", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!currentUserFound) {
            JOptionPane.showMessageDialog(this, "Your account was not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (dbFile.delete() && tempFile.renameTo(dbFile)) {
            logTransaction(currentUser, amount);
            JOptionPane.showMessageDialog(this, "Transfer successful.");
        } else {
            JOptionPane.showMessageDialog(this, "File processing error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String updateLineWithBalance(String[] data, double newBalance) {
        data[10] = String.format("%.2f", newBalance);
        return String.join(", ", data);
    }

    private void logTransaction(String username, double amount) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transaction_history.txt", true))) {
            bw.write(String.format("%s, Bank Transfer, %.2f, %s", username, amount, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())));
            bw.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "File processing error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}