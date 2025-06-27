import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Deposits {
    private JFrame depositFrame;
    private JLabel lblPass, lblAmntDep, lblTitle;
    private JTextField txtAmntDep;
    private JPasswordField passField;
    private JButton btnDeposit, btnBack;
    private final Font helvetica = new Font("Helvetica", Font.PLAIN, 20);

    public Deposits(String loginUser, String loginPass) {
        depositFrame = new JFrame("BancLite - Deposit Form");
        depositFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        depositFrame.setSize(1024, 768);
        depositFrame.setResizable(false);
        depositFrame.setLocationRelativeTo(null);
        depositFrame.setLayout(new BorderLayout());
        depositFrame.setBackground(new Color(135, 206, 235));
        depositFrame.add(depositPanel(loginUser, loginPass));
        depositFrame.setVisible(true);

    }

    private JPanel depositPanel(String loginUser, String loginPass) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.setBackground(new Color(135, 206, 235));

        JPanel depositPanel = new JPanel();
        depositPanel.setLayout(new GridLayout(2, 2, 20, 20));
        depositPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        depositPanel.setBackground(new Color(135, 206, 235));

        JPanel depositBtnPanel = new JPanel();
        depositBtnPanel.setLayout(new BoxLayout(depositBtnPanel, BoxLayout.Y_AXIS));
        depositBtnPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        depositBtnPanel.setBackground(new Color(135, 206, 235));

        lblTitle = new JLabel("Deposit Form");
        lblTitle.setFont(new Font("Helvetica", Font.BOLD, 48));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        lblTitle.setBackground(new Color(135, 206, 235));
        lblTitle.setOpaque(true);

        lblAmntDep = new JLabel("Amount to Deposit: ");
        lblAmntDep.setFont(helvetica);
        lblAmntDep.setHorizontalAlignment(JLabel.CENTER);
        lblAmntDep.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblAmntDep.setBackground(new Color(135, 206, 235));

        lblPass = new JLabel("Confirm password: ");
        lblPass.setFont(helvetica);
        lblPass.setHorizontalAlignment(JLabel.CENTER);
        lblPass.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblPass.setBackground(new Color(135, 206, 235));

        txtAmntDep = new JTextField(10);
        txtAmntDep.setFont(helvetica);
        txtAmntDep.setHorizontalAlignment(JTextField.CENTER);
        //txtAmntDep.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        passField = new JPasswordField(10);
        passField.setFont(helvetica);
        passField.setHorizontalAlignment(JPasswordField.CENTER);
        //passField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnDeposit = new JButton("Deposit");
        btnDeposit.setFont(helvetica);
        btnDeposit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeposit.setMaximumSize(new Dimension(200, 40));
        btnDeposit.setMinimumSize(new Dimension(200, 40));
        btnDeposit.setPreferredSize(new Dimension(200, 40));
        btnDeposit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnDeposit.setBackground(new Color(0, 0, 128));
                btnDeposit.setForeground(Color.WHITE);
                btnDeposit.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnDeposit.setBackground(new JButton().getBackground());
                btnDeposit.setForeground(Color.BLACK);
                btnDeposit.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });

        btnDeposit.addActionListener(e -> {
            String amnt = txtAmntDep.getText();
            String pass = String.valueOf(passField.getPassword());

            try {
                double depAmnt = Double.parseDouble(amnt);
                String hashedInput = CredentialListing.hashPassword(pass.trim());
                File inputFile = new File("AcctDatabase.txt");
                File tempFile = new File("acct_database_temp.txt");

                BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

                String currentLine;
                boolean found = false;
                while ((currentLine = br.readLine()) != null) {
                    String[] lineData = currentLine.split("\\s*,\\s*");
                    
                    if (lineData[7].trim().equals(hashedInput)) {
                        if (lineData.length <= 10) {
                            String[] expandedData = new String[11];
                            System.arraycopy(lineData, 0, expandedData, 0, lineData.length);
                            if (lineData.length <= 10) {
                                expandedData[11] = "0.00";
                            }
                            lineData = expandedData;
                        }
                        
                        // Parse current balance from index 11
                        double currentBalance = 0.00;
                        try {
                            if (lineData.length > 10 && lineData[10] != null && !lineData[10].trim().isEmpty()) {
                                currentBalance = Double.parseDouble(lineData[10].trim());
                            }
                        } catch (NumberFormatException | NullPointerException ex) {
                                currentBalance = 0.00;}

                        double newBalance = currentBalance + depAmnt;
                        if (lineData.length > 10){
                            lineData[10] = String.valueOf(newBalance);
                        }


                        String newLine = String.join(",", lineData);
                        bw.write(newLine);
                        bw.newLine();
                        found = true;
                    } else {
                        bw.write(currentLine);
                        bw.newLine();
                    }
                }
                bw.close();
                br.close();

                if (found) {
                    inputFile.delete();
                    tempFile.renameTo(inputFile);

                    BufferedWriter historyWriter = new BufferedWriter(new FileWriter("transaction_history.txt", true));
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                    historyWriter.write(loginUser + ", Deposit, " + depAmnt + ", " + timestamp);
                    historyWriter.newLine();
                    historyWriter.close();

                    JOptionPane.showMessageDialog(depositFrame, "Deposit Successful!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    depositFrame.dispose();
                    new BancLiteInterface(loginUser, loginPass);
                } else {
                    JOptionPane.showMessageDialog(depositFrame, "Account not found!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(depositFrame, "Invalid Amount!", "Error!", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(depositFrame, "Error accessing account file! Please try again later.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack = new JButton("Go Back");
        btnBack.setFont(helvetica);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.setMaximumSize(new Dimension(200, 40));
        btnBack.setMinimumSize(new Dimension(200, 40));
        btnBack.setPreferredSize(new Dimension(200, 40));
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(new Color(0, 0, 128));
                btnBack.setForeground(Color.WHITE);
                btnBack.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(new JButton().getBackground());
                btnBack.setForeground(Color.BLACK);
                btnBack.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });

        btnBack.addActionListener(e -> {
            depositFrame.dispose();
            new BancLiteInterface(loginUser, loginPass);
        });



        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblTitle, gbc);

        gbc.gridy = 1;
        mainPanel.add(depositPanel, gbc);

        gbc.gridy = 2;
        mainPanel.add(depositBtnPanel, gbc);

        depositPanel.add(lblAmntDep);
        depositPanel.add(txtAmntDep);
        depositPanel.add(lblPass);
        depositPanel.add(passField);

        depositBtnPanel.add(btnDeposit);
        depositBtnPanel.add(Box.createVerticalStrut(25));
        depositBtnPanel.add(btnBack);

        return mainPanel;
    }
}