import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    public enum Type {
        DEPOSIT,
        WITHDRAWAL,
        BALANCE_ENQUIRY
    }

    private String transactionId;
    private Type type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String accountNumber;

    public Transaction(String transactionId, Type type, double amount,
                       double balanceAfter, String accountNumber) {

        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.accountNumber = accountNumber;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return timestamp.format(formatter);
    }

    @Override
    public String toString() {

        return String.format(
                "%-10s %-18s %10.2f %12.2f %s",
                transactionId,
                type,
                amount,
                balanceAfter,
                getFormattedDate()
        );
    }
}