import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.*;
import java.util.Random;

/**
 * Login and Registration form for BancLite offline
 * @author Vin Belandres - Kaito Fujimori
 */

public class CredentialListing {
    //initialize components
    private final JFrame credFrame;
    private final CardLayout cardLayout;
    private final JPanel credPanel;
    private final Font helvetica = new Font("Helvetica", Font.PLAIN, 20);

    //initializing shared components
    public JTextField loginUser;
    public JPasswordField loginPass;

    public CredentialListing() {
        //instantiate components
        credFrame = new JFrame("BancLite - Login and Registration Form");
        credFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        credFrame.setSize(1024, 768);
        credFrame.setResizable(false);
        credFrame.setLocationRelativeTo(null);
        credFrame.setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        credPanel = new JPanel(cardLayout);

        //Adding the panel components
        credPanel.add(createLoginPanel(), "Login");
        credPanel.add(createRegistrationPanel(), "Register");

        credFrame.add(credPanel, BorderLayout.CENTER);
        credFrame.setVisible(true);

    }

    //-------  Login Panel Creation ------
    private JPanel createLoginPanel() {
        JToggleButton loginPassShow;

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        loginPanel.setBounds(0, 0, 512, 384);
        loginPanel.setBackground(new Color(135, 206, 235));

        //top image icon logo
        ImageIcon icon = new ImageIcon(getClass().getResource("BancLite.png"));
        if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
            icon = new ImageIcon("BancLite.png");
        }
        Image scaled = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaled));
        logo.setBounds(300, 50, 400, 300);

        //text fields for username and password
        loginUser = new JTextField(10);
        loginUser.setBounds(500, 325, 250, 30);
        loginUser.setFont(helvetica);

        loginPass = new JPasswordField(10);
        loginPass.setBounds(500, 375, 250, 30);
        loginPass.setFont(helvetica);

        JLabel userLabel = new JLabel("Username: ");
        userLabel.setBounds(310, 325, 125, 30);
        userLabel.setFont(helvetica);

        JLabel passLabel = new JLabel("Password: ");
        passLabel.setBounds(310, 375, 125, 30);
        passLabel.setFont(helvetica);

        //panel for btnLogin, RegLink
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(435, 450, 125, 35);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setBackground(new Color(135, 206, 235));

        //Link to Registration Form
        JLabel registerLink = new JLabel("<HTML><U>Register</U></HTML>");
        registerLink.setFont(helvetica);
        registerLink.setForeground(Color.BLUE);
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.setBounds(463, 500, 100, 30);
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(credPanel, "Register");
            }
        });

        JLabel adminLink = new JLabel("<HTML><U>Admin</U></HTML>");
        adminLink.setFont(helvetica);
        adminLink.setForeground(Color.BLUE);
        adminLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        adminLink.setBounds(463, 550, 100, 30);
        adminLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPasswordField adminPass = new JPasswordField(10);
                int option = JOptionPane.showConfirmDialog(null, adminPass, "Enter Admin Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                    String enteredPassword = new String(adminPass.getPassword());

                    if ("kahitTresLang".equals(enteredPassword)) {
                        credFrame.dispose();
                        new Admin();
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect password. Please try again.");
                    }
                }
            }
        });

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(helvetica);
        loginBtn.setBounds(0, 0, 125, 35);
        loginBtn.addActionListener(e -> checkLogin());

        loginPassShow = new JToggleButton("\uD83D\uDC41");
        loginPassShow.setBounds(755, 375, 50, 30);
        loginPassShow.addActionListener(e -> {
            if (loginPassShow.isSelected()) {
                loginPass.setEchoChar((char) 0);
            } else {
                loginPass.setEchoChar('\u2022');
            }
        });

        //adding components to their respective panels
        buttonPanel.add(loginBtn);
        loginPanel.add(logo);
        loginPanel.add(userLabel);
        loginPanel.add(loginUser);

        loginPanel.add(passLabel);
        loginPanel.add(loginPass);
        loginPanel.add(loginPassShow);

        //adding buttonPanel to loginPanel
        loginPanel.add(buttonPanel);
        loginPanel.add(registerLink);
        loginPanel.add(adminLink);

        return loginPanel;
    }

    //-------  Registration Panel Creation ------
    private JPanel createRegistrationPanel() {
        //Components
        JTextField txtname, txtage, txtbday, txtaddress, txtnationality, txtuser;
        JPasswordField passField, confirmPassField;
        JCheckBox privacyCheck;
        JComboBox<String> gender;
        JPanel regPanel, imagePanel, formPanel, interactPanel, passPanel, confirmPassPanel;
        JButton regBtn;
        JLabel regTitle, loginLink, imageLabel, lblname, lblage, lblbday, lbladdress, lblgender, lblnationality, lbluser, lblpass, lblConfirmPass;
        GridBagConstraints gbc;
        JToggleButton showPassToggle, showConfirmPassToggle;

        //Main Panel for Reg Form
        regPanel = new JPanel();
        gbc = new GridBagConstraints();
        regPanel.setLayout(new GridBagLayout());
        regPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        regPanel.setBackground(new Color(135, 206, 235));

        //Instantiate Form Panel includes the form.
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(10, 2, 7, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 0, 20));
        formPanel.setBackground(new Color(135, 206, 235));

        //Link to login form, register buttons, and privacy check box panel
        interactPanel = new JPanel();
        interactPanel.setLayout(new BoxLayout(interactPanel, BoxLayout.Y_AXIS));
        interactPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 100, 10));
        interactPanel.setBackground(new Color(135, 206, 235));

        //Left Image Marketing Pic Panel
        ImageIcon marketPic = new ImageIcon(getClass().getResource("BancLiteMarketImg.png"));
        if (marketPic.getImageLoadStatus() == MediaTracker.ERRORED) {
            marketPic = new ImageIcon("BancLiteMarketImg.png");
        }
        Image scaled = marketPic.getImage().getScaledInstance(595, 768, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(scaled));
        imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(new Color(135, 206, 235));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        //Constraints for where to put the Image Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.75;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        regPanel.add(imagePanel, gbc);

        //Instantiate Form Fields
        txtname = new JTextField(10);
        txtname.setFont(helvetica);
        txtname.setPreferredSize(new Dimension(125, 30));
        txtage = new JTextField(10);
        txtage.setFont(helvetica);
        txtage.setPreferredSize(new Dimension(125, 30));
        txtbday = new JTextField(10);
        txtbday.setFont(helvetica);
        txtbday.setPreferredSize(new Dimension(125, 30));
        txtaddress = new JTextField(10);
        txtaddress.setFont(helvetica);
        txtaddress.setPreferredSize(new Dimension(125, 30));
        gender = new JComboBox<>(new String[]{"Male", "Female"});
        gender.setFont(helvetica);
        gender.setPreferredSize(new Dimension(125, 30));
        txtnationality = new JTextField(10);
        txtnationality.setFont(helvetica);
        txtnationality.setPreferredSize(new Dimension(125, 30));
        txtuser = new JTextField(10);
        txtuser.setFont(helvetica);
        txtuser.setPreferredSize(new Dimension(125, 30));

        passPanel = new JPanel(new BorderLayout());
        passField = new JPasswordField(10);
        passField.setFont(helvetica);
        passField.setPreferredSize(new Dimension(125, 30));
        passField.setEchoChar('\u2022');
        showPassToggle = new JToggleButton("\uD83D\uDC41");
        showPassToggle.setPreferredSize(new Dimension(50,30));
        showPassToggle.setFocusable(false);
        showPassToggle.addActionListener(e -> {
            passField.setEchoChar(showPassToggle.isSelected() ? (char) 0 : '\u2022');
        });

        passPanel.add(passField, BorderLayout.CENTER);
        passPanel.add(showPassToggle, BorderLayout.EAST);

        confirmPassPanel = new JPanel(new BorderLayout());
        confirmPassField = new JPasswordField(10);
        confirmPassField.setFont(helvetica);
        confirmPassField.setPreferredSize(new Dimension(125, 30));
        confirmPassField.setEchoChar('\u2022');
        showConfirmPassToggle = new JToggleButton("\uD83D\uDC41");
        showConfirmPassToggle.setPreferredSize(new Dimension(50,30));
        showConfirmPassToggle.addActionListener(e -> {
            confirmPassField.setEchoChar(showConfirmPassToggle.isSelected() ? (char) 0 : '\u2022');
        });
        confirmPassPanel.add(confirmPassField, BorderLayout.CENTER);
        confirmPassPanel.add(showConfirmPassToggle, BorderLayout.EAST);


        privacyCheck = new JCheckBox("I accept the terms and conditions.");
        privacyCheck.setFont(helvetica);
        privacyCheck.setBackground(new Color(135, 206, 235));

        //Registration Form Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(135, 206, 235));
        regTitle = new JLabel("Registration Form");
        regTitle.setFont(helvetica.deriveFont(Font.BOLD, 24));
        titlePanel.add(regTitle);

        //Instantiate Form Labels
        lblname = new JLabel("Name ");
        lblname.setFont(helvetica);
        lblage = new JLabel("Age ");
        lblage.setFont(helvetica);
        lblbday = new JLabel("Birthday (mm-dd-yyyy) ");
        lblbday.setFont(helvetica);
        lbladdress = new JLabel("Address ");
        lbladdress.setFont(helvetica);
        lblgender = new JLabel("Gender ");
        lblgender.setFont(helvetica);
        lblnationality = new JLabel("Nationality ");
        lblnationality.setFont(helvetica);
        lbluser = new JLabel("Username ");
        lbluser.setFont(helvetica);
        lblpass = new JLabel("Password ");
        lblpass.setFont(helvetica);
        lblConfirmPass = new JLabel("Confirm Password ");
        lblConfirmPass.setFont(helvetica);

        //Register Button (added to interactPanel)
        regBtn = new JButton("Register");
        regBtn.setFont(helvetica);
        regBtn.setPreferredSize(new Dimension(125, 35));
        regBtn.addActionListener(e -> {
            if (!privacyCheck.isSelected()) {
                JOptionPane.showMessageDialog(null, "Please check the box to register.");
                return;
            }

            //Check if all fields are filled in
            if (txtname.getText().trim().isEmpty() ||
                    txtage.getText().trim().isEmpty() ||
                    txtbday.getText().trim().isEmpty() ||
                    txtaddress.getText().trim().isEmpty() ||
                    txtnationality.getText().trim().isEmpty() ||
                    txtuser.getText().trim().isEmpty() ||
                    passField.getPassword().length == 0 ||
                    confirmPassField.getPassword().length == 0) {

                JOptionPane.showMessageDialog(null, "Please fill out all the required fields.");
                return;
            }

            if (!new String(passField.getPassword()).equals(new String(confirmPassField.getPassword()))) {
                JOptionPane.showMessageDialog(null, "Passwords do not match.");
                return;
            }

            String acctNo = generateAcctNo();
            //Adds the user input into an application database in a CSV format.
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("applications.txt", true))) {
                String data = String.join(" , ",
                        txtname.getText(),
                        txtage.getText(),
                        txtbday.getText(),
                        txtaddress.getText(),
                        (String) gender.getSelectedItem(),
                        txtnationality.getText(),
                        txtuser.getText(),
                        hashPassword(new String(passField.getPassword())),
                        acctNo);

                writer.write(data);
                writer.newLine();
                JOptionPane.showMessageDialog(null, "Account created successfully.\nAccount Number: " + acctNo);
                cardLayout.show(credPanel, "Login");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving account. Please try again");
            }
        });



        //Link to return to Login Form
        loginLink = new JLabel("<HTML><U>Login</U><HTML>");
        loginLink.setFont(helvetica);
        loginLink.setForeground(Color.BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(credPanel, "Login");
            }
        });


        //Add Components to Form Panel
        formPanel.add(lblname);
        formPanel.add(txtname);
        formPanel.add(lblage);
        formPanel.add(txtage);
        formPanel.add(lblbday);
        formPanel.add(txtbday);
        formPanel.add(lbladdress);
        formPanel.add(txtaddress);
        formPanel.add(lblgender);
        formPanel.add(gender);
        formPanel.add(lblnationality);
        formPanel.add(txtnationality);
        formPanel.add(lbluser);
        formPanel.add(txtuser);

        formPanel.add(lblpass);
        formPanel.add(passPanel);

        formPanel.add(lblConfirmPass);
        formPanel.add(confirmPassPanel);

        //Alignment for interactPanel
        privacyCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Panel for Login Link
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(135, 206, 235));
        loginPanel.add(loginLink);

        //Adding the link, privacy check box, and register button to interactPanel
        interactPanel.add(privacyCheck);
        interactPanel.add(Box.createVerticalStrut(3));
        interactPanel.add(loginPanel);
        interactPanel.add(Box.createVerticalStrut(3));
        interactPanel.add(regBtn);

        //Constraints for Title Panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        gbc.weighty = 0.1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        regPanel.add(titlePanel, gbc);

        //Constraints for Form Panel
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.25;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        regPanel.add(formPanel, gbc);

        //Constraints for Interact Panel
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.25;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        regPanel.add(interactPanel, gbc);

        return regPanel;
    }

    //-------  Login Check ------
    private void checkLogin() {
        String username = loginUser.getText();
        String password = new String(loginPass.getPassword());
        File inputFile = new File("AcctDatabase.txt");
        File tempFile = new File("Acct_Database_Temp.txt");
        boolean userFound = false;
        boolean loginSuccess = false;

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");

                 if (parts.length >= 10){
                     String fileUsername = parts[6].trim();
                     String filePassword = parts[7].trim();
                     String status = parts[9].trim();
                     String hashedInput = hashPassword(password);
                     if (fileUsername.trim().equals(username) && filePassword.trim().equals(hashedInput)) {
                         userFound = true;
                         if (status.equalsIgnoreCase("Approve")) {
                             if (parts.length == 10 || parts[10].trim().isEmpty()){
                                 line = line  + ", 0.00";
                             }
                             loginSuccess = true;
                             JOptionPane.showMessageDialog(null, "Login successful!");
                             credFrame.dispose();
                             new BancLiteInterface(username, password);
                         } else {
                             JOptionPane.showMessageDialog(null, "Account might not be approved yet or deactivated.");
                         }
                     }
                }
                 writer.write(line);
                 writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading account file.");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        if(!userFound){
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
        }
    }

    public static boolean validate(String password) {
        String hashedInput = hashPassword(password);
        try (BufferedReader reader = new BufferedReader(new FileReader("AcctDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");
                if (parts.length >= 8 && parts[7].trim().equals(hashedInput)) {
                    return true;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private String generateAcctNo(){
        StringBuilder acctnum = new StringBuilder();
        Random rand = new Random();
        for(int i = 0; i < 10; i++){
            acctnum.append(rand.nextInt(10));
        }
        return acctnum.toString();
    }
}
