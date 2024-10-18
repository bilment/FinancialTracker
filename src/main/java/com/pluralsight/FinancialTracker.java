package com.pluralsight;

import java.io.*;
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
                    scanner.nextLine();
                    break;
                case "P":
                    addPayment(scanner);
                    scanner.nextLine();
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

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Creating LocalDate and LocalTime with Date and time.
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);

                // Getting other fields as Vendor and Amount
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                // Creating new Transaction object and add to arraylist.
                transactions.add(new Transaction(date, time, description, vendor, amount));


            }


        } catch (Exception e) {
            System.out.println("Error has occurred:");
            e.printStackTrace();
        }
    }

    private static void addDeposit(Scanner scanner) {


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

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount);
            bw.newLine();
            System.out.println("Deposit added.");
        } catch (Exception e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }

    }

    private static void addPayment(Scanner scanner) {

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

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount);
            bw.newLine();
            System.out.println("Payment added.");
        } catch (Exception e) {
            System.out.println("An error has occured. ");
            e.printStackTrace();
        }
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
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {

        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private static void displayDeposits() {

        System.out.println("Date | Time | Description | Vendor | Amount (Deposits)");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void displayPayments() {

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

                    LocalDate startOfCurrentMonth = LocalDate.now().withDayOfMonth(1);
                    filterTransactionsByDate(startOfCurrentMonth, LocalDate.now());
                    break;

                case "2":

                    LocalDate startOfPreviousMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    LocalDate endOfPreviousMonth = LocalDate.now().withDayOfMonth(1).minusDays(1);
                    filterTransactionsByDate(startOfPreviousMonth, endOfPreviousMonth);
                    break;

                case "3":

                    LocalDate startOfCurrentYear = LocalDate.now().withDayOfYear(1);
                    filterTransactionsByDate(startOfCurrentYear, LocalDate.now());
                    break;

                case "4":

                    LocalDate startOfPreviousYear = LocalDate.now().minusYears(1).withDayOfYear(1);
                    LocalDate endOfPreviousYear = LocalDate.now().withDayOfYear(1).minusDays(1);
                    filterTransactionsByDate(startOfPreviousYear, endOfPreviousYear);
                    break;

                case "5":

                    System.out.println("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor);
                    break;

                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {

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