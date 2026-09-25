import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ATM {

    // Fixed menu stored using an array.
    public static final String[] MAIN_MENU = {
            "Balance Enquiry",
            "Deposit Money",
            "Withdraw Money",
            "Mini Statement",
            "Transaction History",
            "Account Search",
            "Reports",
            "Logout"
    };

    private ArrayList<Customer> customers;

    private HashMap<String, Account> accountsByNumber;

    private TreeMap<String, Account> accountsSortedByNumber;

    private static final int MAX_PIN_ATTEMPTS = 3;

    public ATM() {

        customers = new ArrayList<>();
        accountsByNumber = new HashMap<>();
        accountsSortedByNumber = new TreeMap<>();
    }

    // -----------------------------
    // CRUD - CREATE CUSTOMER
    // -----------------------------

    public void addCustomer(Customer customer) {

        customers.add(customer);
    }

    // -----------------------------
    // CREATE ACCOUNT
    // -----------------------------

    public void openAccount(Account account)
            throws InvalidTransactionException {

        if (accountsByNumber.containsKey(
                account.getAccountNumber())) {

            throw new InvalidTransactionException(
                    "Account number already exists."
            );
        }

        accountsByNumber.put(
                account.getAccountNumber(),
                account
        );

        accountsSortedByNumber.put(
                account.getAccountNumber(),
                account
        );
    }

    // -----------------------------
    // SEARCH ACCOUNT
    // -----------------------------

    public Account searchAccount(String accountNumber)
            throws InvalidTransactionException {

        Account account =
                accountsByNumber.get(accountNumber);

        if (account == null) {

            throw new InvalidTransactionException(
                    "Account " + accountNumber +
                            " does not exist."
            );
        }

        return account;
    }

    // -----------------------------
    // SEARCH CUSTOMER BY NAME
    // -----------------------------

    public List<Account> searchByCustomerName(String name) {

        List<Account> result = new ArrayList<>();

        for (Account account : accountsByNumber.values()) {

            if (account.getCustomer()
                    .getName()
                    .toLowerCase()
                    .contains(name.toLowerCase())) {

                result.add(account);
            }
        }

        return result;
    }

    // -----------------------------
    // LOGIN
    // -----------------------------

    public Account login(
            String accountNumber,
            String pin)
            throws InvalidPinException,
            InvalidTransactionException {

        Account account = accountsByNumber.get(accountNumber);

        if (account == null) {

            throw new InvalidTransactionException(
                    "Account does not exist."
            );
        }

        for (int attempt = 1;
             attempt <= MAX_PIN_ATTEMPTS;
             attempt++) {

            if (account.getCustomer().validatePin(pin)) {
                return account;
            }

            if (attempt == MAX_PIN_ATTEMPTS) {

                throw new InvalidPinException(
                        "ATM blocked. Three incorrect PIN attempts."
                );
            }

            throw new InvalidPinException(
                    "Invalid PIN. Attempt "
                            + attempt
                            + " of "
                            + MAX_PIN_ATTEMPTS
            );
        }

        throw new InvalidPinException("Login failed.");
    }

    // -----------------------------
    // BALANCE ENQUIRY
    // -----------------------------

    public double checkBalance(Account account) {

        account.recordBalanceEnquiry();

        return account.getBalance();
    }

    // -----------------------------
    // DEPOSIT
    // -----------------------------

    public void deposit(
            Account account,
            double amount)
            throws InvalidTransactionException {

        account.deposit(amount);
    }

    // -----------------------------
    // WITHDRAW
    // -----------------------------

    public void withdraw(
            Account account,
            double amount)
            throws InvalidTransactionException,
            InsufficientBalanceException {

        account.withdraw(amount);
    }

    // -----------------------------
    // DELETE ACCOUNT
    // -----------------------------

    public void deleteAccount(String accountNumber)
            throws InvalidTransactionException {

        Account removed =
                accountsByNumber.remove(accountNumber);

        if (removed == null) {

            throw new InvalidTransactionException(
                    "Account does not exist."
            );
        }

        accountsSortedByNumber.remove(accountNumber);
    }

    // -----------------------------
    // GET ALL CUSTOMERS
    // -----------------------------

    public List<Customer> getAllCustomers() {

        return new ArrayList<>(customers);
    }

    // -----------------------------
    // GET ALL ACCOUNTS
    // -----------------------------

    public List<Account> getAllAccounts() {

        return new ArrayList<>(
                accountsByNumber.values()
        );
    }

    // -----------------------------
    // SORT ACCOUNTS BY BALANCE
    // -----------------------------

    public List<Account> sortAccountsByBalance() {

        List<Account> accounts =
                new ArrayList<>(
                        accountsByNumber.values()
                );

        accounts.sort(
                (a, b) -> Double.compare(
                        b.getBalance(),
                        a.getBalance()
                )
        );

        return accounts;
    }

    // -----------------------------
    // SORT ACCOUNTS BY NUMBER
    // -----------------------------

    public List<Account> sortAccountsByNumber() {

        return new ArrayList<>(
                accountsSortedByNumber.values()
        );
    }

    // -----------------------------
    // SEARCH TRANSACTION
    // -----------------------------

    public Transaction searchTransaction(
            Account account,
            String transactionId) {

        for (Transaction transaction :
                account.getTransactionHistory()) {

            if (transaction.getTransactionId()
                    .equalsIgnoreCase(transactionId)) {

                return transaction;
            }
        }

        return null;
    }

    // -----------------------------
    // BANK SUMMARY REPORT
    // -----------------------------

    public String generateSummaryReport() {

        double totalBalance = 0;
        int totalTransactions = 0;

        for (Account account :
                accountsSortedByNumber.values()) {

            totalBalance += account.getBalance();

            totalTransactions +=
                    account.getTransactionHistory().size();
        }

        StringBuilder report =
                new StringBuilder();

        report.append(
                "========== ATM BANK SUMMARY REPORT ==========\n"
        );

        report.append(
                String.format(
                        "Total Registered Customers : %d%n",
                        customers.size()
                )
        );

        report.append(
                String.format(
                        "Total Active Accounts       : %d%n",
                        accountsByNumber.size()
                )
        );

        report.append(
                String.format(
                        "Total Bank Balance          : Rs. %.2f%n",
                        totalBalance
                )
        );

        report.append(
                String.format(
                        "Total Transactions Logged   : %d%n",
                        totalTransactions
                )
        );

        return report.toString();
    }

    // -----------------------------
    // ACCOUNTS REPORT
    // -----------------------------

    public String generateAccountsReport() {

        StringBuilder report =
                new StringBuilder();

        report.append(
                "================ ACCOUNT REPORT ================\n"
        );

        report.append(
                String.format(
                        "%-12s %-25s %15s%n",
                        "Account No.",
                        "Customer",
                        "Balance"
                )
        );

        report.append(
                "-------------------------------------------------\n"
        );

        for (Account account :
                accountsSortedByNumber.values()) {

            report.append(
                    String.format(
                            "%-12s %-25s Rs. %10.2f%n",
                            account.getAccountNumber(),
                            account.getCustomer().getName(),
                            account.getBalance()
                    )
            );
        }

        return report.toString();
    }
}