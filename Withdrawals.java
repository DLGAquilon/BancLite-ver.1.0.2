import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Withdrawals {
    JFrame withdrawFrame;
    private final Font helvetica = new Font("Helvetica", Font.PLAIN, 20);

    public Withdrawals(String loginUser, String loginPass) {
        withdrawFrame = new JFrame("BancLite - Withdrawals Form");
        withdrawFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        withdrawFrame.setSize(1024, 768);
        withdrawFrame.setResizable(false);
        withdrawFrame.setLocationRelativeTo(null);
        withdrawFrame.setLayout(new BorderLayout());
        withdrawFrame.setBackground(new Color(135, 206, 235));
        withdrawFrame.add(withdrawPanel(loginUser, loginPass));
        withdrawFrame.setVisible(true);
    }

    private JPanel withdrawPanel(String loginUser, String loginPass) {
        JLabel lblTitle, lblAmntWdrw, lblPin;
        JTextField txtAmntWdrw, txtPin;
        JButton btnWithdraw, btnPin, btnBack;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.setBackground(new Color(135, 206, 235));

        JPanel withdrawalPanel = new JPanel();
        withdrawalPanel.setLayout(new GridLayout(2, 2, 20, 20));
        withdrawalPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        withdrawalPanel.setBackground(new Color(135, 206, 235));

        JPanel btnWithdrawPanel = new JPanel();
        btnWithdrawPanel.setBackground(new Color(135, 206, 235));
        btnWithdrawPanel.setLayout(new BoxLayout(btnWithdrawPanel, BoxLayout.Y_AXIS));


        lblTitle = new JLabel("Withdrawals Form");
        lblTitle.setFont(helvetica.deriveFont(Font.BOLD, 48));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);

        lblAmntWdrw = new JLabel("Enter withdraw amount: ");
        lblAmntWdrw.setFont(helvetica);
        lblAmntWdrw.setHorizontalAlignment(JLabel.CENTER);
        lblAmntWdrw.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblAmntWdrw.setBackground(new Color(135, 206, 235));

        lblPin = new JLabel("Enter OTP: ");
        lblPin.setFont(helvetica);
        lblPin.setHorizontalAlignment(JLabel.CENTER);
        lblPin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblPin.setBackground(new Color(135, 206, 235));

        txtAmntWdrw = new JTextField(10);
        txtAmntWdrw.setFont(helvetica);
        txtAmntWdrw.setHorizontalAlignment(JTextField.CENTER);


        txtPin = new JTextField(10);
        txtPin.setFont(helvetica);
        txtPin.setHorizontalAlignment(JTextField.CENTER);


        btnPin = new JButton("Generate OTP");
        btnPin.setMaximumSize(new Dimension(200, 40));
        btnPin.setMinimumSize(new Dimension(200, 40));
        btnPin.setPreferredSize(new Dimension(200, 40));
        btnPin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPin.setFont(helvetica);

        btnWithdraw = new JButton("Withdraw");
        btnWithdraw.setFont(helvetica);
        btnWithdraw.setMaximumSize(new Dimension(200, 40));
        btnWithdraw.setMinimumSize(new Dimension(200, 40));
        btnWithdraw.setPreferredSize(new Dimension(200, 40));
        btnWithdraw.setAlignmentX(Component.CENTER_ALIGNMENT);


        btnBack = new JButton("Go Back");
        btnBack.setFont(helvetica);
        btnBack.setMaximumSize(new Dimension(200, 40));
        btnBack.setMinimumSize(new Dimension(200, 40));
        btnBack.setPreferredSize(new Dimension(200, 40));
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnPin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnPin.setBackground(new Color(0, 0, 128));
                btnPin.setForeground(Color.WHITE);
                btnPin.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPin.setBackground(new JButton().getBackground());
                btnPin.setForeground(Color.BLACK);
                btnPin.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });

        btnWithdraw.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnWithdraw.setBackground(new Color(0, 0, 128));
                btnWithdraw.setForeground(Color.WHITE);
                btnWithdraw.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnWithdraw.setBackground(new JButton().getBackground());
                btnWithdraw.setForeground(Color.BLACK);
                btnWithdraw.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });

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

        final String[] generateOtp = {null};
        btnPin.addActionListener(e -> {
            int otp = 100000 + new Random().nextInt(900000);
            generateOtp[0] = String.valueOf(otp);
            JOptionPane.showMessageDialog(mainPanel, "OTP: " + generateOtp[0], "Success!", JOptionPane.INFORMATION_MESSAGE);
        });

        btnWithdraw.addActionListener(e -> {
            String amnt = txtAmntWdrw.getText().trim();
            String enterPin = txtPin.getText().trim();

            if (generateOtp[0] == null) {
                JOptionPane.showMessageDialog(mainPanel, "Please generate OTP first!", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!generateOtp[0].equals(enterPin)) {
                JOptionPane.showMessageDialog(mainPanel, "Incorrect OTP!", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try{
                double withdrawAmount = Double.parseDouble(amnt);
                File inputFile = new File("AcctDatabase.txt");
                File tempFile = new File("acct_database_temp.txt");

                BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
                String line;
                boolean found = false;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\s*,\\s*");
                    if (parts[6].trim().equals(loginUser.trim())) {
                        double currentBalance = Double.parseDouble(parts[10].trim());
                        if (withdrawAmount > currentBalance) {
                            JOptionPane.showMessageDialog(mainPanel, "Insufficient balance.", "Error!", JOptionPane.ERROR_MESSAGE);
                            bw.write(line);
                            bw.newLine();
                            continue;
                        }

                        double newBalance = currentBalance - withdrawAmount;
                        parts[10] = String.valueOf(newBalance);
                        String updateLine = String.join(",", parts);
                        bw.write(updateLine);
                        bw.newLine();
                        found = true;
                    } else {
                        bw.write(line);
                        bw.newLine();
                    }
                }
                br.close();
                bw.close();

                if (found) {
                    inputFile.delete();
                    tempFile.renameTo(inputFile);

                    BufferedWriter historyWriter = new BufferedWriter(new FileWriter("transaction_history.txt", true));
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                    historyWriter.write(loginUser + ", Withdraw, " + withdrawAmount + ", " + timestamp);
                    historyWriter.newLine();
                    historyWriter.close();
                    
                    JOptionPane.showMessageDialog(mainPanel, "Withdrawal Successful!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    withdrawFrame.dispose();
                    new BancLiteInterface(loginUser, loginPass);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Account not found!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Invalid Amount!", "Error!", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainPanel, "Error accessing account! Please try again later.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> {
            withdrawFrame.dispose();
            new BancLiteInterface(loginUser, loginPass);
        });

        withdrawalPanel.add(lblAmntWdrw); withdrawalPanel.add(txtAmntWdrw);
        withdrawalPanel.add(lblPin); withdrawalPanel.add(txtPin);

        btnWithdrawPanel.add(btnPin);
        btnWithdrawPanel.add(Box.createVerticalStrut(25));
        btnWithdrawPanel.add(btnWithdraw);
        btnWithdrawPanel.add(Box.createVerticalStrut(25));
        btnWithdrawPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 0; mainPanel.add(lblTitle, gbc);
        gbc.gridy = 1; mainPanel.add(withdrawalPanel, gbc);
        gbc.gridy = 2; mainPanel.add(btnWithdrawPanel, gbc);


        return mainPanel;
    }
}
