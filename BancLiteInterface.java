import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class BancLiteInterface {
    private JFrame mainFrame;
    private JLabel greetingLabel, transactionLabel;
    private JButton depositButton, withdrawButton, statementButton, transferButton;
    private JPanel topPanel, buttonPanel, imagePanel;
    private final Font helvetica = new Font("Helvetica", Font.PLAIN, 20);

    public BancLiteInterface(String loginUser, String loginPass) {
        mainFrame = new JFrame("BancLite");
        mainFrame.setSize(1024, 768);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setBackground(new Color(135, 206, 235));

        // Top panel for labels
        topPanel = new JPanel(new BorderLayout());
        greetingLabel = new JLabel("Hello, " + loginUser + "!");
        greetingLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
        greetingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        transactionLabel = new JLabel("Please select the desired transaction", SwingConstants.CENTER);
        transactionLabel.setFont(helvetica);


        topPanel.setBackground(new Color(135, 206, 235));
        topPanel.add(greetingLabel, BorderLayout.WEST);
        topPanel.add(transactionLabel, BorderLayout.CENTER);

        imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(new Color(135, 206, 235));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Add deposit marketing image
        ImageIcon depositIcon = new ImageIcon("depositmarketing.png");
        JLabel depositImgLabel = new JLabel();
        depositImgLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(depositImgLabel);
        imagePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = imagePanel.getWidth();
                int height = imagePanel.getHeight() - depositButton.getHeight();
                Image scaledImage = depositIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                depositImgLabel.setIcon(new ImageIcon(scaledImage));
            }
        });


        // Button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
        buttonPanel.setBackground(new Color(135, 206, 235));


        depositButton = new JButton("Deposits");
        depositButton.setPreferredSize(new Dimension(200, 40));
        depositButton.setMaximumSize(new Dimension(200, 40));
        depositButton.setMinimumSize(new Dimension(200, 40));
        depositButton.setFont(helvetica);
        depositButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                depositButton.setBackground(new Color(0, 0, 128));
                depositButton.setForeground(Color.WHITE);
                depositButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                depositButton.setBackground(new JButton().getBackground());
                depositButton.setForeground(Color.BLACK);
                depositButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                depositButton.setPreferredSize(new Dimension(150, 40));
            }
        });

        depositButton.addActionListener(e -> {
            mainFrame.dispose();
            new Deposits(loginUser, loginPass);
        });


        withdrawButton = new JButton("Withdrawals");
        withdrawButton.setFont(helvetica);
        withdrawButton.setPreferredSize(new Dimension(200, 40));
        withdrawButton.setMaximumSize(new Dimension(200, 40));
        withdrawButton.setMinimumSize(new Dimension(200, 40));
        withdrawButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                withdrawButton.setBackground(new Color(0, 0, 128));
                withdrawButton.setForeground(Color.WHITE);
                withdrawButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                withdrawButton.setBackground(new JButton().getBackground());
                withdrawButton.setForeground(Color.BLACK);
                withdrawButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                withdrawButton.setPreferredSize(new Dimension(150, 40));
            }
        });

        withdrawButton.addActionListener(e -> {
            mainFrame.dispose();
            new Withdrawals(loginUser, loginPass);
        });

        statementButton = new JButton("Bank Statement");
        statementButton.setFont(helvetica);
        statementButton.setPreferredSize(new Dimension(200, 40));
        statementButton.setMaximumSize(new Dimension(200, 40));
        statementButton.setMinimumSize(new Dimension(200, 40));
        statementButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statementButton.setBackground(new Color(0, 0, 128));
                statementButton.setForeground(Color.WHITE);
                statementButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                statementButton.setBackground(new JButton().getBackground());
                statementButton.setForeground(Color.BLACK);
                statementButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                statementButton.setPreferredSize(new Dimension(150, 40));
            }
        });

        statementButton.addActionListener(e -> {
            mainFrame.dispose();
            new BankStatement(loginUser, loginPass);
        });

        transferButton = new JButton("Transfer");
        transferButton.setFont(helvetica);
        transferButton.setPreferredSize(new Dimension(200, 40));
        transferButton.setMaximumSize(new Dimension(200, 40));
        transferButton.setMinimumSize(new Dimension(200, 40));
        transferButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                transferButton.setBackground(new Color(0, 0, 128));
                transferButton.setForeground(Color.WHITE);
                transferButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                transferButton.setBackground(new JButton().getBackground());
                transferButton.setForeground(Color.BLACK);
                transferButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                transferButton.setPreferredSize(new Dimension(150, 40));
            }
        });

        transferButton.addActionListener(e -> {
            mainFrame.dispose();
            new BankTransfer(loginUser);
        });


        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(statementButton);
        buttonPanel.add(transferButton);

        mainFrame.add(topPanel, BorderLayout.NORTH);
        mainFrame.add(imagePanel, BorderLayout.CENTER);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }
}
   
