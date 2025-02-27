package com.bankmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class BankManagementSystem {
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the login screen
        SwingUtilities.invokeLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}

class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Bank Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Bank logo/name
        
        JLabel logoLabel = new JLabel("SECURE BANK", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(0, 87, 141));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 87, 141));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(e -> {
            // For demo purposes, accept any non-empty username/password
            if (usernameField.getText().trim().length() > 0 &&
                    passwordField.getPassword().length > 0) {
                dispose();
                MainDashboard dashboard = new MainDashboard();
                dashboard.setVisible(true);
                dashboard.validate();
                dashboard.repaint();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid credentials",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to form panel
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Add components to main panel
        mainPanel.add(logoLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(loginButton, BorderLayout.SOUTH);

        // Set content pane
        setContentPane(mainPanel);
    }
}

class MainDashboard extends JFrame {
    private JPanel mainContent;
    private CardLayout cardLayout;
    private BankDatabase database;
    private JTable accountsTable;
    private JTable transactionsTable;

    public MainDashboard() {
        // Initialize the database
        database = new BankDatabase();

        setTitle("Bank Management System");
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main content area with CardLayout
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);

        // Create the sidebar
        JPanel sidebar = createSidebar();

        // Create the various screens
        JPanel dashboardPanel = createDashboardPanel();
        JPanel accountsPanel = createAccountsPanel();
        JPanel transactionsPanel = createTransactionsPanel();
        JPanel customersPanel = createCustomersPanel();
        JPanel reportsPanel = createReportsPanel();

        // Add screens to the card layout
        mainContent.add(dashboardPanel, "dashboard");
        mainContent.add(accountsPanel, "accounts");
        mainContent.add(transactionsPanel, "transactions");
        mainContent.add(customersPanel, "customers");
        mainContent.add(reportsPanel, "reports");

        // Show dashboard first
        cardLayout.show(mainContent, "dashboard");

        // Set up main layout
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 87, 141));
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Bank Management System");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Admin User");
        userLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            dispose();
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });

        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(40, 44, 52));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 0, 0));

        String[] menuItems = { "Dashboard", "Accounts", "Transactions", "Customers", "Reports" };
        String[] screens = { "dashboard", "accounts", "transactions", "customers", "reports" };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i]);
            String screenName = screens[i];
            menuButton.addActionListener(e -> cardLayout.show(mainContent, screenName));
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return sidebar;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(40, 44, 52));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(200, 40));
        button.setPreferredSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(54, 60, 70));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(40, 44, 52));
            }
        });

        return button;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));

        // Total accounts card
        JPanel accountsCard = createStatCard("Total Accounts", "156", new Color(41, 128, 185));

        // Active customers card
        JPanel customersCard = createStatCard("Active Customers", "142", new Color(39, 174, 96));

        // Total deposits card
        JPanel depositsCard = createStatCard("Total Deposits", "$2,587,412.00", new Color(243, 156, 18));

        // Total loans card
        JPanel loansCard = createStatCard("Total Loans", "$1,248,365.00", new Color(192, 57, 43));

        statsPanel.add(accountsCard);
        statsPanel.add(customersCard);
        statsPanel.add(depositsCard);
        statsPanel.add(loansCard);

        // Recent activities panel
        JPanel activitiesPanel = new JPanel(new BorderLayout());
        activitiesPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel activitiesTitle = new JLabel("Recent Activities");
        activitiesTitle.setFont(new Font("Arial", Font.BOLD, 18));

        String[] columns = { "Transaction ID", "Customer", "Type", "Amount", "Date" };
        Object[][] data = {
                { "TRX-001", "John Smith", "Deposit", "$1,500.00", "2025-02-25" },
                { "TRX-002", "Maria Garcia", "Withdrawal", "$500.00", "2025-02-25" },
                { "TRX-003", "Robert Johnson", "Transfer", "$2,000.00", "2025-02-24" },
                { "TRX-004", "Sarah Williams", "Loan Payment", "$350.00", "2025-02-24" },
                { "TRX-005", "David Brown", "Deposit", "$5,000.00", "2025-02-23" }
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);

        activitiesPanel.add(activitiesTitle, BorderLayout.NORTH);
        activitiesPanel.add(scrollPane, BorderLayout.CENTER);

        // Add components to main panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(activitiesPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Accounts Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("Add Account");
        addButton.setBackground(new Color(39, 174, 96));
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false);

        addButton.addActionListener(e -> {
            // Show account creation dialog
            JDialog dialog = new JDialog(this, "Create New Account", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);

            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            formPanel.add(new JLabel("Customer Name:"));
            JTextField nameField = new JTextField();
            formPanel.add(nameField);

            formPanel.add(new JLabel("Account Type:"));
            String[] accountTypes = { "Savings", "Checking", "Fixed Deposit", "Loan" };
            JComboBox<String> typeCombo = new JComboBox<>(accountTypes);
            formPanel.add(typeCombo);

            formPanel.add(new JLabel("Initial Balance:"));
            JTextField balanceField = new JTextField();
            formPanel.add(balanceField);

            formPanel.add(new JLabel("Interest Rate (%):"));
            JTextField rateField = new JTextField();
            formPanel.add(rateField);

            JButton saveButton = new JButton("Create Account");
            saveButton.addActionListener(event -> {
                // Add to database
                Account newAccount = new Account(
                        "ACC-" + (100 + database.getAccounts().size()),
                        nameField.getText(),
                        typeCombo.getSelectedItem().toString(),
                        Double.parseDouble(balanceField.getText()),
                        Double.parseDouble(rateField.getText()));
                database.addAccount(newAccount);

                // Update account table
                updateAccountsTable();

                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Account created successfully!");
            });

            formPanel.add(new JLabel(""));
            formPanel.add(saveButton);

            dialog.add(formPanel);
            dialog.setVisible(true);
        });

        JButton editButton = new JButton("Edit");
        editButton.setFocusPainted(false);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(192, 57, 43));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setFocusPainted(false);

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);

        // Accounts table
        String[] columns = { "Account No", "Customer", "Type", "Balance", "Interest Rate", "Status" };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        accountsTable = new JTable(tableModel); // Save reference to the table
        JScrollPane scrollPane = new JScrollPane(accountsTable);

        // Load initial accounts
        updateAccountsTable();

        // Add components to main panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(toolbar, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
    }

    private void updateAccountsTable() {
        DefaultTableModel model = (DefaultTableModel) accountsTable.getModel();

        // Clear existing rows
        model.setRowCount(0);

        // Add all accounts
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        for (Account account : database.getAccounts()) {
            model.addRow(new Object[] {
                    account.getAccountNumber(),
                    account.getCustomerName(),
                    account.getAccountType(),
                    currencyFormat.format(account.getBalance()),
                    account.getInterestRate() + "%",
                    "Active"
            });
        }
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Transactions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Transaction form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        formPanel.add(new JLabel("Transaction Type:"));
        String[] transactionTypes = { "Deposit", "Withdrawal", "Transfer" };
        JComboBox<String> typeCombo = new JComboBox<>(transactionTypes);
        formPanel.add(typeCombo);

        formPanel.add(new JLabel("Account Number:"));
        JComboBox<String> accountCombo = new JComboBox<>();
        for (Account account : database.getAccounts()) {
            accountCombo.addItem(account.getAccountNumber() + " - " + account.getCustomerName());
        }
        formPanel.add(accountCombo);

        formPanel.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        formPanel.add(amountField);

        JButton processButton = new JButton("Process Transaction");
        processButton.setBackground(new Color(41, 128, 185));
        processButton.setForeground(Color.BLACK);
        processButton.setFocusPainted(false);

        processButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String accountNumber = accountCombo.getSelectedItem().toString().split(" - ")[0];
                String transactionType = typeCombo.getSelectedItem().toString();

                // Create and add transaction
                Transaction transaction = new Transaction(
                        "TRX-" + (1000 + database.getTransactions().size()),
                        accountNumber,
                        transactionType,
                        amount,
                        new Date());
                database.addTransaction(transaction);

                // Update account balance
                Account account = database.findAccount(accountNumber);
                if (account != null) {
                    if (transactionType.equals("Deposit")) {
                        account.setBalance(account.getBalance() + amount);
                    } else if (transactionType.equals("Withdrawal")) {
                        account.setBalance(account.getBalance() - amount);
                    }
                }

                // Update transaction table
                updateTransactionsTable();

                // Clear form
                amountField.setText("");

                JOptionPane.showMessageDialog(this, "Transaction processed successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel(""));
        formPanel.add(processButton);

        // Transactions table
        String[] columns = { "Transaction ID", "Account", "Type", "Amount", "Date", "Status" };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        transactionsTable = new JTable(tableModel); // Save reference to the table
        JScrollPane scrollPane = new JScrollPane(transactionsTable);

        // Load initial transactions
        updateTransactionsTable();

        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void updateTransactionsTable() {
        DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();

        // Clear existing rows
        model.setRowCount(0);

        // Add all transactions
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Transaction transaction : database.getTransactions()) {
            model.addRow(new Object[] {
                    transaction.getTransactionId(),
                    transaction.getAccountNumber(),
                    transaction.getType(),
                    currencyFormat.format(transaction.getAmount()),
                    dateFormat.format(transaction.getDate()),
                    "Completed"
            });
        }
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setFocusPainted(false);
        searchPanel.add(searchButton);

        // Customers table
        String[] columns = { "ID", "Name", "Email", "Phone", "Address", "Accounts" };
        Object[][] data = {
                { "CUST-001", "John Smith", "john.smith@email.com", "(555) 123-4567", "123 Main St, Anytown", "2" },
                { "CUST-002", "Maria Garcia", "maria.garcia@email.com", "(555) 234-5678", "456 Oak Ave, Someville",
                        "1" },
                { "CUST-003", "Robert Johnson", "robert.j@email.com", "(555) 345-6789", "789 Pine Rd, Otherburg", "3" },
                { "CUST-004", "Sarah Williams", "sarah.w@email.com", "(555) 456-7890", "321 Maple Dr, Newcity", "1" },
                { "CUST-005", "David Brown", "david.b@email.com", "(555) 567-8901", "654 Cedar Ln, Oldtown", "2" }
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);

        // Customer detail panel
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel customerInfo = new JPanel(new GridLayout(3, 2, 10, 10));
        customerInfo.setBorder(new EmptyBorder(0, 0, 10, 0));

        customerInfo.add(new JLabel("Customer ID:"));
        customerInfo.add(new JLabel("CUST-001"));

        customerInfo.add(new JLabel("Name:"));
        customerInfo.add(new JLabel("John Smith"));

        customerInfo.add(new JLabel("Contact:"));
        customerInfo.add(new JLabel("(555) 123-4567"));

        JPanel accountsPanel = new JPanel(new BorderLayout());
        JLabel accountsLabel = new JLabel("Accounts");
        accountsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String[] accColumns = { "Account Number", "Type", "Balance", "Status" };
        Object[][] accData = {
                { "ACC-001", "Savings", "$12,450.75", "Active" },
                { "ACC-002", "Checking", "$3,275.50", "Active" }
        };

        JTable accTable = new JTable(new DefaultTableModel(accData, accColumns));
        JScrollPane accScrollPane = new JScrollPane(accTable);

        accountsPanel.add(accountsLabel, BorderLayout.NORTH);
        accountsPanel.add(accScrollPane, BorderLayout.CENTER);

        detailPanel.add(customerInfo, BorderLayout.NORTH);
        detailPanel.add(accountsPanel, BorderLayout.CENTER);

        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(detailPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Report selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Report Type:"));

        String[] reportTypes = {
                "Daily Transaction Summary",
                "Monthly Account Balance",
                "Customer Activity",
                "Loan Report"
        };
        JComboBox<String> reportCombo = new JComboBox<>(reportTypes);
        selectionPanel.add(reportCombo);

        selectionPanel.add(new JLabel("Date Range:"));
        selectionPanel.add(new JTextField("2025-02-01", 8));
        selectionPanel.add(new JLabel("to"));
        selectionPanel.add(new JTextField("2025-02-25", 8));

        JButton generateButton = new JButton("Generate Report");
        generateButton.setBackground(new Color(41, 128, 185));
        generateButton.setForeground(Color.BLACK);
        generateButton.setFocusPainted(false);
        selectionPanel.add(generateButton);

        // Report content
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel reportTitle = new JLabel("Daily Transaction Summary (Feb 25, 2025)");
        reportTitle.setFont(new Font("Arial", Font.BOLD, 18));

        // Summary cards
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));

        JPanel depositsCard = createStatCard("Total Deposits", "$15,785.00", new Color(39, 174, 96));
        JPanel withdrawalsCard = createStatCard("Total Withdrawals", "$8,432.00", new Color(192, 57, 43));
        JPanel transfersCard = createStatCard("Total Transfers", "$23,650.00", new Color(41, 128, 185));

        summaryPanel.add(depositsCard);
        summaryPanel.add(withdrawalsCard);
        summaryPanel.add(transfersCard);

        // Transactions table
        String[] columns = { "Transaction ID", "Time", "Account", "Type", "Amount" };
        Object[][] data = {
                { "TRX-001", "09:15 AM", "ACC-001", "Deposit", "$1,500.00" },
                { "TRX-002", "10:22 AM", "ACC-003", "Withdrawal", "$500.00" },
                { "TRX-003", "11:05 AM", "ACC-005", "Transfer", "$2,000.00" },
                { "TRX-004", "01:30 PM", "ACC-002", "Loan Payment", "$350.00" },
                { "TRX-005", "02:45 PM", "ACC-004", "Deposit", "$5,000.00" },
                { "TRX-006", "03:20 PM", "ACC-001", "Withdrawal", "$1,000.00" },
                { "TRX-007", "04:10 PM", "ACC-003", "Transfer", "$750.00" }
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);

        // Export buttons
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton pdfButton = new JButton("Export as PDF");
        pdfButton.setFocusPainted(false);

        JButton excelButton = new JButton("Export as Excel");
        excelButton.setFocusPainted(false);

        JButton printButton = new JButton("Print");
        printButton.setFocusPainted(false);

        exportPanel.add(pdfButton);
        exportPanel.add(excelButton);
        exportPanel.add(printButton);

        // Assemble report panel
        JPanel reportContent = new JPanel(new BorderLayout());
        reportContent.add(summaryPanel, BorderLayout.NORTH);
        reportContent.add(scrollPane, BorderLayout.CENTER);
        reportContent.add(exportPanel, BorderLayout.SOUTH);

        reportPanel.add(reportTitle, BorderLayout.NORTH);
        reportPanel.add(reportContent, BorderLayout.CENTER);

        // Add components to main panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(selectionPanel, BorderLayout.CENTER);
        panel.add(reportPanel, BorderLayout.SOUTH);

        return panel;
    }
}

class BankDatabase {
    private List<Account> accounts;
    private List<Transaction> transactions;

    public BankDatabase() {
        // Initialize with sample data
        accounts = new ArrayList<>();
        accounts.add(new Account("ACC-001", "John Smith", "Savings", 12450.75, 2.5));
        accounts.add(new Account("ACC-002", "John Smith", "Checking", 3275.50, 0.5));
        accounts.add(new Account("ACC-003", "Maria Garcia", "Savings", 8750.25, 2.5));
        accounts.add(new Account("ACC-004", "Robert Johnson", "Fixed Deposit", 50000.00, 5.0));
        accounts.add(new Account("ACC-005", "Robert Johnson", "Savings", 15320.80, 2.5));
        accounts.add(new Account("ACC-006", "Robert Johnson", "Loan", 25000.00, 7.5));
        accounts.add(new Account("ACC-007", "Sarah Williams", "Checking", 2840.15, 0.5));
        accounts.add(new Account("ACC-008", "David Brown", "Savings", 18650.30, 2.5));
        accounts.add(new Account("ACC-009", "David Brown", "Fixed Deposit", 30000.00, 5.0));

        transactions = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            transactions.add(new Transaction("TRX-1001", "ACC-001", "Deposit", 1500.00, sdf.parse("2025-02-25 09:15")));
            transactions
                    .add(new Transaction("TRX-1002", "ACC-003", "Withdrawal", 500.00, sdf.parse("2025-02-25 10:22")));
            transactions
                    .add(new Transaction("TRX-1003", "ACC-005", "Transfer", 2000.00, sdf.parse("2025-02-25 11:05")));
            transactions
                    .add(new Transaction("TRX-1004", "ACC-002", "Loan Payment", 350.00, sdf.parse("2025-02-25 13:30")));
            transactions.add(new Transaction("TRX-1005", "ACC-004", "Deposit", 5000.00, sdf.parse("2025-02-25 14:45")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}

class Account {
    private String accountNumber;
    private String customerName;
    private String accountType;
    private double balance;
    private double interestRate;

    public Account(String accountNumber, String customerName, String accountType, double balance, double interestRate) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.accountType = accountType;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterestRate() {
        return interestRate;
    }
}

class Transaction {
    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private Date date;

    public Transaction(String transactionId, String accountNumber, String type, double amount, Date date) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}