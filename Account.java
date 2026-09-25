import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Account {

    private String accountNumber;
    private Customer customer;
    private double balance;

    private LinkedList<Transaction> transactionHistory;

    private static final double MINIMUM_BALANCE = 500.00;
    private static final double MAX_WITHDRAWAL = 50000.00;

    private int transactionCounter = 1;

    public Account(String accountNumber, Customer customer, double openingBalance)
            throws InvalidTransactionException {

        if (openingBalance < MINIMUM_BALANCE) {
            throw new InvalidTransactionException(
                    "Opening balance must be at least Rs. 500."
            );
        }

        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = openingBalance;

        transactionHistory = new LinkedList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBalance() {
        return balance;
    }

    public LinkedList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void deposit(double amount)
            throws InvalidTransactionException {

        if (amount <= 0) {
            throw new InvalidTransactionException(
                    "Deposit amount must be greater than zero."
            );
        }

        balance += amount;

        addTransaction(
                Transaction.Type.DEPOSIT,
                amount
        );
    }

    public void withdraw(double amount)
            throws InvalidTransactionException,
            InsufficientBalanceException {

        if (amount <= 0) {
            throw new InvalidTransactionException(
                    "Withdrawal amount must be greater than zero."
            );
        }

        if (amount % 100 != 0) {
            throw new InvalidTransactionException(
                    "Withdrawal amount must be in multiples of Rs. 100."
            );
        }

        if (amount > MAX_WITHDRAWAL) {
            throw new InsufficientBalanceException(
                    "Withdrawal exceeds per-transaction limit of Rs. "
                            + MAX_WITHDRAWAL
            );
        }

        if (balance - amount < MINIMUM_BALANCE) {
            throw new InsufficientBalanceException(
                    "Withdrawal denied. Minimum balance of Rs. "
                            + MINIMUM_BALANCE + " must be maintained."
            );
        }

        balance -= amount;

        addTransaction(
                Transaction.Type.WITHDRAWAL,
                amount
        );
    }

    public void recordBalanceEnquiry() {

        addTransaction(
                Transaction.Type.BALANCE_ENQUIRY,
                0
        );
    }

    private void addTransaction(Transaction.Type type, double amount) {

        String transactionId =
                String.format("TXN%04d", transactionCounter++);

        Transaction transaction = new Transaction(
                transactionId,
                type,
                amount,
                balance,
                accountNumber
        );

        // Newest transaction is kept at the beginning.
        transactionHistory.addFirst(transaction);
    }

    public List<Transaction> getMiniStatement() {

        int count = Math.min(5, transactionHistory.size());

        return new LinkedList<>(
                transactionHistory.subList(0, count)
        );
    }

    public List<Transaction> sortTransactionsByAmount() {

        List<Transaction> sorted =
                new LinkedList<>(transactionHistory);

        sorted.sort(
                Comparator.comparingDouble(
                        Transaction::getAmount
                ).reversed()
        );

        return sorted;
    }

    public List<Transaction> sortTransactionsByDate() {

        List<Transaction> sorted =
                new LinkedList<>(transactionHistory);

        sorted.sort(
                Comparator.comparing(
                        Transaction::getTimestamp
                ).reversed()
        );

        return sorted;
    }
}