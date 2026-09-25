import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        ATM atm = new ATM();

        try {

            // -----------------------------------------
            // Customer 1
            // -----------------------------------------

            Customer customer1 =
                    new Customer(
                            "CUST01",
                            "Rohit",
                            "9876543210",
                            "1111"
                    );

            Account account1 =
                    new Account(
                            "100001",
                            customer1,
                            25000.00
                    );

            atm.addCustomer(customer1);
            atm.openAccount(account1);

            // -----------------------------------------
            // Customer 2
            // -----------------------------------------

            Customer customer2 =
                    new Customer(
                            "CUST02",
                            "Niharika",
                            "9876543211",
                            "2222"
                    );

            Account account2 =
                    new Account(
                            "100002",
                            customer2,
                            15000.00
                    );

            atm.addCustomer(customer2);
            atm.openAccount(account2);

            // -----------------------------------------
            // Customer 3
            // -----------------------------------------

            Customer customer3 =
                    new Customer(
                            "CUST03",
                            "Aarav",
                            "9876543212",
                            "3333"
                    );

            Account account3 =
                    new Account(
                            "100003",
                            customer3,
                            50000.00
                    );

            atm.addCustomer(customer3);
            atm.openAccount(account3);

            // -----------------------------------------
            // Customer 4
            // -----------------------------------------

            Customer customer4 =
                    new Customer(
                            "CUST04",
                            "Saanvi",
                            "9876543213",
                            "4444"
                    );

            Account account4 =
                    new Account(
                            "100004",
                            customer4,
                            8000.00
                    );

            atm.addCustomer(customer4);
            atm.openAccount(account4);

        } catch (InvalidTransactionException e) {

            System.out.println(
                    "Error while creating sample data: "
                            + e.getMessage()
            );

            return;
        }

        // Start the Swing application on
        // the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {

            ATMSimulatorGUI gui =
                    new ATMSimulatorGUI(atm);

            gui.setVisible(true);
        });
    }
}