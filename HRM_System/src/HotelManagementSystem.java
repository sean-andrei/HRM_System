import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class HotelManagementSystem {
    static String customerFileName = "Customer_Records.txt";
    static String ordersFileName = "Customer_Orders.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        createFile(customerFileName); // Ensure customer records file exists
        createFile(ordersFileName);   // Ensure orders records file exists
        mainMenu();
    }

    // Main Navigation Menu
    private static void mainMenu() {
        while (true) {
            System.out.println("\n=== HOTEL MANAGEMENT SYSTEM ===");
            System.out.println("[A] Customer Record Module");
            System.out.println("[B] Reservation Record Module");
            System.out.println("[C] Billing Module");
            System.out.println("[X] Exit System");
            System.out.print("Select an option: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "A" -> customerRecordModule();
                case "B" -> reservationRecordModule();
                case "C" -> billingModule(); // Billing functionality added
                case "X" -> {
                    System.out.println("Exiting system. Thank you!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Billing Module
    private static void billingModule() {
        try {
            File orderFile = new File(ordersFileName);
            if (orderFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
                    String line;
                    double totalBill = 0.0;
                    
                    System.out.print("Enter Customer Name to calculate bill: ");
                    String customerName = scanner.nextLine();
                    
                    System.out.println("\n=== BILLING MODULE ===");
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("Customer: " + customerName)) {
                            // Parsing the relevant order details
                            String[] parts = line.split(", ");
                            String itemName = parts[1].split(": ")[1];
                            String quantity = parts[2].split(": ")[1];
                            String cost = parts[3].split(": ")[1].replace("PHP ", "");
                            
                            double itemCost = Double.parseDouble(cost);
                            totalBill += itemCost;
                            System.out.println("Item: " + itemName + ", Quantity: " + quantity + ", Cost: PHP " + itemCost);
                        }
                    }
                    
                    System.out.println("\n--- Bill Summary ---");
                    System.out.println("Total Order Cost: PHP " + totalBill);
                }
            } else {
                System.out.println("No orders record found for billing.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while processing the billing module.");
        }
    }

    // Customer Record Module
    private static void customerRecordModule() {
        while (true) {
            System.out.println("\n=== CUSTOMER RECORD MODULE ===");
            System.out.println("[A] Create Customer Record");
            System.out.println("[B] Update Customer Record");
            System.out.println("[C] View Customer Records");
            System.out.println("[X] Return to Main Menu");
            System.out.print("Select an option: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "A" -> createCustomerRecord();
                case "B" -> updateCustomerRecord();
                case "C" -> viewCustomerRecords();
                case "X" -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Reservation Record Module
    private static void reservationRecordModule() {
        while (true) {
            System.out.println("\n=== RESERVATION RECORD MODULE ===");
            System.out.println("[A] Create Orders Record");
            System.out.println("[B] View Orders Record");
            System.out.println("[X] Return to Main Menu");
            System.out.print("Select an option: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "A" -> createOrdersRecord();
                case "B" -> viewOrdersRecord();
                case "X" -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Create Orders Record
    private static void createOrdersRecord() {
        Map<String, Integer> menu = new LinkedHashMap<>();
        menu.put("Burger", 200);
        menu.put("Pasta", 250);
        menu.put("Steak", 600);
        menu.put("Salad", 150);
        menu.put("Pizza", 400);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ordersFileName, true))) {
            System.out.println("\n=== MEALS MENU ===");
            System.out.println("Item\t\tPrice (PHP)");
            menu.forEach((item, price) -> System.out.println(item + "\t\t" + price));

            System.out.print("\nEnter Customer Name: ");
            String customerName = scanner.nextLine();

            System.out.println("\nStart adding items to the order. Type 'DONE' to finish.");

            double totalCost = 0.0;
            while (true) {
                System.out.print("Enter Item Name: ");
                String itemName = scanner.nextLine();

                if (itemName.equalsIgnoreCase("DONE")) break;

                if (menu.containsKey(itemName)) {
                    System.out.print("Enter Quantity: ");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    int itemCost = menu.get(itemName) * quantity;
                    totalCost += itemCost;

                    writer.write("Customer: " + customerName + ", Item: " + itemName + ", Quantity: " + quantity + ", Cost: PHP " + itemCost);
                    writer.newLine();
                } else {
                    System.out.println("Invalid item. Please choose an item from the menu.");
                }
            }

            writer.write("Total Cost for " + customerName + ": PHP " + totalCost);
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            System.out.println("Order created successfully! Total Cost: PHP " + totalCost);
        } catch (IOException e) {
            System.out.println("An error occurred while creating the orders record.");
        }
    }

    // View Orders Record
    private static void viewOrdersRecord() {
        File file = new File(ordersFileName);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                System.out.println("\n=== ORDERS RECORD ===");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while viewing the orders record.");
            }
        } else {
            System.out.println("No orders record found.");
        }
    }

    // View Customer Records
    private static void viewCustomerRecords() {
        File file = new File(customerFileName);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                System.out.println("\n=== CUSTOMER RECORDS ===");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while viewing the customer records.");
            }
        } else {
            System.out.println("No customer records found.");
        }
    }

    // Create Customer Record
    private static void createCustomerRecord() {
        Map<Integer, String> rooms = new LinkedHashMap<>();
        rooms.put(1, "Standard Single Room (PHP 1000)");
        rooms.put(2, "Standard Single Room (PHP 1000)");
        rooms.put(3, "Standard Single Room (PHP 1000)");
        rooms.put(4, "Standard Single Room (PHP 1000)");
        rooms.put(5, "Standard Double Room (PHP 2500)");
        rooms.put(6, "Standard Double Room (PHP 2500)");
        rooms.put(7, "Standard Double Room (PHP 2500)");
        rooms.put(8, "Junior Suite (PHP 4500)");
        rooms.put(9, "Junior Suite (PHP 4500)");
        rooms.put(10, "Executive Suite (PHP 5000)");
        rooms.put(11, "Executive Suite (PHP 5000)");
        rooms.put(12, "Executive Suite (PHP 5000)");
        rooms.put(13, "Executive Suite (PHP 5000)");
        rooms.put(14, "Presidential Suite (PHP 8000)");
        rooms.put(15, "Presidential Suite (PHP 8000)");
        rooms.put(16, "Presidential Suite (PHP 8000)");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customerFileName, true))) {
            System.out.println("\n=== ROOMS AVAILABLE ===");
            rooms.forEach((key, value) -> System.out.println("Room " + key + ": " + value));

            System.out.print("\nEnter Customer Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Customer Age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Customer Address: ");
            String address = scanner.nextLine();

            System.out.print("Enter Contact Number: ");
            String contact = scanner.nextLine();

            System.out.print("Enter Room Number: ");
            int roomNum = Integer.parseInt(scanner.nextLine());

            if (rooms.containsKey(roomNum)) {
                writer.write("Customer: " + name + ", Age: " + age + ", Address: " + address + ", Contact: " + contact + ", Room: " + rooms.get(roomNum));
                writer.newLine();
                System.out.println("Customer record created successfully!");
            } else {
                System.out.println("Invalid room number. Please select a valid room.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the customer record.");
        }
    }

    // Update Customer Record
    private static void updateCustomerRecord() {
        // Implement update logic if required
    }

    // Helper Method: Create File if not Exists
    private static void createFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println(fileName + " created successfully.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating " + fileName + ".");
        }
    }
}
