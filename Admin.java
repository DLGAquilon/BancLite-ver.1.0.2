import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Admin extends JFrame{
    private final JTable table;
    private final DefaultTableModel model;
    private final String[] statuses = {"Pending", "Approve", "Declined"};
    private final Font monospace = new Font("Monospaced", Font.PLAIN, 14);
    JButton saveButton, btnBack;
    JPanel bottomPanel;
    JScrollPane scrollPane;
    TableColumn statusColumn;
    JTableHeader header;

    public Admin() {
        setTitle("BancLite - Admin");
        setSize(1920, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        String[] columns = {"Name", "Address", "Birthday", "Address", "Gender", "Nationality", "Username", "Password", "Account No", "Status"};


        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9;
            }
        };
        table = new JTable(model);
        table.setFont(monospace);
        table.setBackground(new Color(135, 206, 235));
        table.setRowHeight(30);

        header = table.getTableHeader();
        header.setBackground(new Color(135, 206, 235));
        header.setFont(monospace);
        header.setForeground(Color.BLACK);


        statusColumn = table.getColumnModel().getColumn(9);
        statusColumn.setCellEditor(new DefaultCellEditor(new JComboBox<>(statuses)));
        loadApplications();
        scrollPane = new JScrollPane(table);

        saveButton = new JButton("Save");
        saveButton.setFont(monospace);
        saveButton.setPreferredSize(new Dimension(100, 30));
        saveButton.setMaximumSize(new Dimension(100, 30));
        saveButton.setMinimumSize(new Dimension(100, 30));
        saveButton.addActionListener(e -> saveApproved());

        btnBack = new JButton("Back");
        btnBack.setFont(monospace);
        btnBack.setPreferredSize(new Dimension(100, 30));
        btnBack.setMaximumSize(new Dimension(100, 30));
        btnBack.setMinimumSize(new Dimension(100, 30));
        btnBack.addActionListener(e -> {
            dispose();
            new CredentialListing();
        });

        bottomPanel = new JPanel();
        bottomPanel.add(saveButton);
        bottomPanel.add(btnBack);
        bottomPanel.setBackground(new Color(135, 206, 235));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void loadApplications() {
        try (BufferedReader br = new BufferedReader(new FileReader("applications.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\s*, \\s*");
                if (data.length == 10) {
                    if (!Arrays.asList(statuses).contains(data[9])) {
                        data[9] = "Pending";
                    }
                    model.addRow(data);
                } else if (data.length == 9) {
                    // Fallback if status is missing
                    Object[] rowData = Arrays.copyOf(data, 10);
                    for (int i = 0; i < 9; i++) {
                        rowData[i] = data[i];
                    }
                    rowData[9] = "Pending"; // Default status
                    model.addRow(rowData);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load applications.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void saveApproved() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("AcctDatabase.txt", true))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String status = (String) model.getValueAt(i, 9).toString();
                if ("Approve".equalsIgnoreCase(status)) {
                    StringBuilder fullData = new StringBuilder();
                    for (int j = 0; j < 10; j++) {
                        fullData.append(model.getValueAt(i, j));
                        if (j < 9) fullData.append(", ");
                    }
                        bw.write(fullData.toString());
                        bw.newLine();
                    }
                }

            JOptionPane.showMessageDialog(this, "Credentials saved successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
