public class Customer {

    private String customerId;
    private String name;
    private String mobileNumber;
    private String pin;

    public Customer(String customerId, String name, String mobileNumber, String pin) {
        this.customerId = customerId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.pin = pin;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public boolean validatePin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    @Override
    public String toString() {
        return customerId + " - " + name + " - " + mobileNumber;
    }
}