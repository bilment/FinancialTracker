package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Creating LocalDateTime with Date and time.
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);

                // Getting other fields as Vendor and Amount
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                // Creating new Transaction object and add to list.
                transactions.add(new Transaction(date, time, description, vendor, amount));


            }


        } catch (Exception e) {
            System.out.println("Error has occurred:");
            e.printStackTrace();
        }
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.


        System.out.println("Enter the date of the deposit (yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.println("Enter the time of the deposit(HH:mm:ss):");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.println("Enter the description:");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor:");
        String vendor = scanner.nextLine();

        System.out.println("Enter the amount of deposit:");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Invalid input. Amount cannot be lower than 0 or equal.");
            return;
        }

        transactions.add(new Transaction(date, time, description, vendor, amount));


    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        System.out.println("Enter the date of the payment (yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.println("Enter the time of the payment(HH:mm:ss):");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.println("Enter the description:");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor:");
        String vendor = scanner.nextLine();

        System.out.println("Enter the amount of payment:");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Invalid input. Payment cannot be lower than 0 or equal.");
            return;
        }

        amount = -Math.abs(amount);

        transactions.add(new Transaction(date, time, description, vendor, amount));

        System.out.println("You made a payment.");
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Date | Time | Description | Vendor | Amount (Deposits)");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Date | Time | Description | Vendor | Amount (Payments)");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        boolean found = false;
        System.out.println("Date | Time | Description | Vendor | Amount");
        System.out.println("-----------------------------------------------------");

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();

            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("There are no results.");
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        boolean found = false;
        System.out.println("Date | Time | Description | Vendor | Amount");
        System.out.println("-----------------------------------------------------");

        for (Transaction transaction : transactions) {

            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found for the specified vendor: " + vendor);
        }
    }
}