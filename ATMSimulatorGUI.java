import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ATMSimulatorGUI extends JFrame {

    private ATM atm;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField accountField;
    private JPasswordField pinField;

    private Account loggedInAccount;

    private int loginAttempts = 0;

    public ATMSimulatorGUI(ATM atm) {

        this.atm = atm;

        setTitle("ATM Transaction Simulator");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createMenuPanel(), "MENU");

        add(mainPanel);

        cardLayout.show(mainPanel, "LOGIN");
    }

    // =====================================================
    // LOGIN SCREEN
    // =====================================================

    private JPanel createLoginPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        JPanel box = new JPanel(
                new GridBagLayout()
        );

        box.setBorder(
                BorderFactory.createTitledBorder(
                        "ATM Customer Login"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        JLabel title =
                new JLabel(
                        "WELCOME TO ATM",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        box.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;

        box.add(
                new JLabel("Account Number:"),
                gbc
        );

        accountField =
                new JTextField(15);

        gbc.gridx = 1;

        box.add(
                accountField,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy++;

        box.add(
                new JLabel("PIN:"),
                gbc
        );

        pinField =
                new JPasswordField(15);

        gbc.gridx = 1;

        box.add(
                pinField,
                gbc
        );

        JButton loginButton =
                new JButton("LOGIN");

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        box.add(
                loginButton,
                gbc
        );

        loginButton.addActionListener(
                e -> login()
        );

        panel.add(box);

        return panel;
    }

    // =====================================================
    // LOGIN METHOD
    // =====================================================

    private void login() {

        String accountNumber =
                accountField.getText().trim();

        String pin =
                new String(
                        pinField.getPassword()
                ).trim();

        if (accountNumber.isEmpty()
                || pin.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter account number and PIN.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            loggedInAccount =
                    atm.login(
                            accountNumber,
                            pin
                    );

            loginAttempts = 0;

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful!\nWelcome, "
                            + loggedInAccount
                            .getCustomer()
                            .getName()
            );

            cardLayout.show(
                    mainPanel,
                    "MENU"
            );

            accountField.setText("");
            pinField.setText("");

        } catch (InvalidPinException e) {

            loginAttempts++;

            int remaining =
                    3 - loginAttempts;

            if (remaining <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Too many incorrect attempts.\nATM access blocked.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                loginAttempts = 0;

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid PIN.\n"
                                + "Attempts remaining: "
                                + remaining,
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (InvalidTransactionException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // MAIN MENU
    // =====================================================

    private JPanel createMenuPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel heading =
                new JLabel(
                        "ATM MAIN MENU",
                        SwingConstants.CENTER
                );

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        panel.add(
                heading,
                BorderLayout.NORTH
        );

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                15,
                                15
                        )
                );

        JButton balanceButton =
                new JButton("Balance Enquiry");

        JButton depositButton =
                new JButton("Deposit Money");

        JButton withdrawButton =
                new JButton("Withdraw Money");

        JButton miniStatementButton =
                new JButton("Mini Statement");

        JButton historyButton =
                new JButton("Transaction History");

        JButton searchButton =
                new JButton("Account Search");

        JButton reportButton =
                new JButton("Reports");

        JButton logoutButton =
                new JButton("Logout");

        buttonPanel.add(balanceButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(miniStatementButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(logoutButton);

        panel.add(
                buttonPanel,
                BorderLayout.CENTER
        );

        balanceButton.addActionListener(
                e -> showBalance()
        );

        depositButton.addActionListener(
                e -> depositMoney()
        );

        withdrawButton.addActionListener(
                e -> withdrawMoney()
        );

        miniStatementButton.addActionListener(
                e -> showMiniStatement()
        );

        historyButton.addActionListener(
                e -> showTransactionHistory()
        );

        searchButton.addActionListener(
                e -> searchAccount()
        );

        reportButton.addActionListener(
                e -> showReports()
        );

        logoutButton.addActionListener(
                e -> logout()
        );

        return panel;
    }

    // =====================================================
    // BALANCE
    // =====================================================

    private void showBalance() {

        double balance =
                atm.checkBalance(
                        loggedInAccount
                );

        JOptionPane.showMessageDialog(
                this,
                String.format(
                        "Account Number: %s%nCurrent Balance: Rs. %.2f",
                        loggedInAccount.getAccountNumber(),
                        balance
                ),
                "Balance Enquiry",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // DEPOSIT
    // =====================================================

    private void depositMoney() {

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter amount to deposit:"
                );

        if (input == null) {
            return;
        }

        try {

            double amount =
                    Double.parseDouble(input);

            atm.deposit(
                    loggedInAccount,
                    amount
            );

            JOptionPane.showMessageDialog(
                    this,
                    String.format(
                            "Deposit successful.%n%n"
                                    + "Amount: Rs. %.2f%n"
                                    + "New Balance: Rs. %.2f",
                            amount,
                            loggedInAccount.getBalance()
                    )
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (InvalidTransactionException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Transaction Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // WITHDRAW
    // =====================================================

    private void withdrawMoney() {

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter amount to withdraw:\n"
                                + "Amount must be a multiple of Rs. 100."
                );

        if (input == null) {
            return;
        }

        try {

            double amount =
                    Double.parseDouble(input);

            atm.withdraw(
                    loggedInAccount,
                    amount
            );

            JOptionPane.showMessageDialog(
                    this,
                    String.format(
                            "Please collect your cash.%n%n"
                                    + "Amount: Rs. %.2f%n"
                                    + "Remaining Balance: Rs. %.2f",
                            amount,
                            loggedInAccount.getBalance()
                    )
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (InvalidTransactionException
                 | InsufficientBalanceException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Withdrawal Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // MINI STATEMENT
    // =====================================================

    private void showMiniStatement() {

        List<Transaction> transactions =
                loggedInAccount.getMiniStatement();

        showTransactionTable(
                transactions,
                "Mini Statement - Last 5 Transactions"
        );
    }

    // =====================================================
    // COMPLETE HISTORY
    // =====================================================

    private void showTransactionHistory() {

        List<Transaction> transactions =
                loggedInAccount
                        .getTransactionHistory();

        showTransactionTable(
                transactions,
                "Complete Transaction History"
        );
    }

    // =====================================================
    // TRANSACTION TABLE
    // =====================================================

    private void showTransactionTable(
            List<Transaction> transactions,
            String title) {

        String[] columns = {
                "Transaction ID",
                "Type",
                "Amount",
                "Balance After",
                "Date & Time"
        };

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                );

        for (Transaction transaction :
                transactions) {

            Object[] row = {

                    transaction.getTransactionId(),

                    transaction.getType(),

                    String.format(
                            "Rs. %.2f",
                            transaction.getAmount()
                    ),

                    String.format(
                            "Rs. %.2f",
                            transaction.getBalanceAfter()
                    ),

                    transaction.getFormattedDate()
            };

            model.addRow(row);
        }

        JTable table =
                new JTable(model);

        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setPreferredSize(
                new Dimension(
                        750,
                        350
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // ACCOUNT SEARCH
    // =====================================================

    private void searchAccount() {

        String[] options = {
                "Search by Account Number",
                "Search by Customer Name"
        };

        int choice =
                JOptionPane.showOptionDialog(
                        this,
                        "Select search method:",
                        "Account Search",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        if (choice == 0) {

            String accountNumber =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter account number:"
                    );

            if (accountNumber == null) {
                return;
            }

            try {

                Account account =
                        atm.searchAccount(
                                accountNumber.trim()
                        );

                JOptionPane.showMessageDialog(
                        this,
                        String.format(
                                "Account Number: %s%n"
                                        + "Customer: %s%n"
                                        + "Balance: Rs. %.2f",
                                account.getAccountNumber(),
                                account.getCustomer().getName(),
                                account.getBalance()
                        ),
                        "Account Found",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (InvalidTransactionException e) {

                JOptionPane.showMessageDialog(
                        this,
                        e.getMessage(),
                        "Search Result",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } else if (choice == 1) {

            String name =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter customer name:"
                    );

            if (name == null) {
                return;
            }

            List<Account> results =
                    atm.searchByCustomerName(name);

            if (results.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No customer found."
                );

                return;
            }

            StringBuilder result =
                    new StringBuilder();

            for (Account account : results) {

                result.append(
                        String.format(
                                "Account: %s%n"
                                        + "Customer: %s%n"
                                        + "Balance: Rs. %.2f%n%n",
                                account.getAccountNumber(),
                                account.getCustomer().getName(),
                                account.getBalance()
                        )
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    result.toString(),
                    "Search Results",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // =====================================================
    // REPORTS
    // =====================================================

    private void showReports() {

        String[] options = {
                "Bank Summary",
                "Accounts by Number",
                "Accounts by Balance",
                "Cancel"
        };

        int choice =
                JOptionPane.showOptionDialog(
                        this,
                        "Select report:",
                        "ATM Reports",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        if (choice == 0) {

            showTextReport(
                    atm.generateSummaryReport(),
                    "Bank Summary Report"
            );

        } else if (choice == 1) {

            showTextReport(
                    atm.generateAccountsReport(),
                    "Accounts Sorted by Number"
            );

        } else if (choice == 2) {

            showBalanceSortedAccounts();
        }
    }

    // =====================================================
    // BALANCE SORTED REPORT
    // =====================================================

    private void showBalanceSortedAccounts() {

        List<Account> accounts =
                atm.sortAccountsByBalance();

        StringBuilder report =
                new StringBuilder();

        report.append(
                "========= ACCOUNTS SORTED BY BALANCE =========\n\n"
        );

        for (Account account : accounts) {

            report.append(
                    String.format(
                            "%-10s %-25s Rs. %.2f%n",
                            account.getAccountNumber(),
                            account.getCustomer().getName(),
                            account.getBalance()
                    )
            );
        }

        showTextReport(
                report.toString(),
                "Accounts by Balance"
        );
    }

    // =====================================================
    // TEXT REPORT
    // =====================================================

    private void showTextReport(
            String text,
            String title) {

        JTextArea area =
                new JTextArea(text);

        area.setEditable(false);

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(area);

        scrollPane.setPreferredSize(
                new Dimension(
                        700,
                        400
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    private void logout() {

        loggedInAccount = null;

        JOptionPane.showMessageDialog(
                this,
                "You have been logged out successfully."
        );

        cardLayout.show(
                mainPanel,
                "LOGIN"
        );
    }
}