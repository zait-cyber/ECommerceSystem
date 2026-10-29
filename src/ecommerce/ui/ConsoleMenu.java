package ecommerce.ui;

import ecommerce.model.Customer;
import ecommerce.model.Employee;
import ecommerce.model.Order;
import ecommerce.model.Product;
import ecommerce.model.Role;
import ecommerce.model.User;
import ecommerce.service.Store;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * All console interaction lives here: reading input with Scanner
 * and showing the Main, Customer and Employee menus.
 */
public class ConsoleMenu {

    private Store store;
    private Scanner scanner;
    private User currentUser;

    public ConsoleMenu(Store store) {
        this.store = store;
        this.scanner = new Scanner(System.in);
    }

    // ===================== MAIN MENU =====================

    public void start() {
        printTitle("CONSOLE E-COMMERCE SYSTEM");
        System.out.println("Demo accounts:  employee  admin@shop.kz / admin123");
        System.out.println("                customer  aruzhan@mail.kz / 1234");

        boolean running = true;
        while (running) {
            printTitle("MAIN MENU");
            System.out.println("1. Login");
            System.out.println("2. Register as a new customer");
            System.out.println("3. Browse products (guest)");
            System.out.println("0. Exit");
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1 -> login();
                case 2 -> registerCustomer();
                case 3 -> store.listProducts();
                case 0 -> running = false;
                default -> System.out.println("Unknown option, try again.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void login() {
        String email = readText("Email: ");
        String password = readText("Password: ");
        User user = store.login(email, password);
        if (user == null) {
            System.out.println("Wrong email or password.");
            return;
        }
        currentUser = user;
        System.out.println("Welcome, " + user.getName() + "!");

        if (user.getRole() == Role.CUSTOMER) {
            customerMenu((Customer) user);
        } else {
            employeeMenu((Employee) user);
        }
        currentUser = null;
    }

    private void registerCustomer() {
        try {
            String name = readText("Full name: ");
            String email = readText("Email: ");
            String password = readText("Password (min 4 chars): ");
            String address = readText("Address: ");
            String phone = readText("Phone: ");
            Customer customer = store.addCustomer(name, email, password, address, phone);
            System.out.println("Registered! Your user ID is " + customer.getId() + ". Now you can log in.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===================== CUSTOMER MENU =====================

    private void customerMenu(Customer customer) {
        boolean loggedIn = true;
        while (loggedIn) {
            printTitle("CUSTOMER MENU  (" + customer.getName() + ")");
            System.out.println("1. List products");
            System.out.println("2. Place an order");
            System.out.println("3. View my orders");
            System.out.println("4. View receipt by order ID");
            System.out.println("5. My profile");
            System.out.println("0. Logout");
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1 -> store.listProducts();
                case 2 -> placeOrder(customer);
                case 3 -> showOrders(store.getOrdersOf(customer));
                case 4 -> showReceipt(customer);
                case 5 -> customer.displayInfo();
                case 0 -> loggedIn = false;
                default -> System.out.println("Unknown option, try again.");
            }
        }
    }

    private void placeOrder(Customer customer) {
        Map<Product, Integer> cart = new LinkedHashMap<>();
        System.out.println("Add products to your cart. Enter 0 as product ID to finish.");

        while (true) {
            store.listProducts();
            int id = readInt("Product ID (0 = finish): ");
            if (id == 0) {
                break;
            }
            Product product = store.findProductById(id);
            if (product == null) {
                System.out.println("Product not found.");
                continue;
            }
            int quantity = readInt("Quantity: ");
            int alreadyInCart = cart.getOrDefault(product, 0);
            if (!product.isInStock(quantity + alreadyInCart)) {
                System.out.println("Sorry, only " + product.getStockQuantity()
                        + " pieces available (" + alreadyInCart + " already in cart).");
                continue;
            }
            cart.put(product, alreadyInCart + quantity);
            System.out.println("Added: " + product.getName() + " x" + quantity);
        }

        if (cart.isEmpty()) {
            System.out.println("Cart is empty, order cancelled.");
            return;
        }

        try {
            Order order = store.placeOrder(customer, cart);
            System.out.println("Order placed successfully!");
            order.printReceipt();
        } catch (IllegalArgumentException e) {
            System.out.println("Order failed: " + e.getMessage());
        }
    }

    private void showReceipt(Customer customer) {
        int id = readInt("Order ID: ");
        Order order = store.findOrderById(id);
        if (order == null || order.getCustomer().getId() != customer.getId()) {
            System.out.println("You have no order with this ID.");
            return;
        }
        order.printReceipt();
    }

    // ===================== EMPLOYEE MENU =====================

    private void employeeMenu(Employee employee) {
        boolean loggedIn = true;
        while (loggedIn) {
            printTitle("EMPLOYEE MENU  (" + employee.getName() + ", " + employee.getPosition() + ")");
            System.out.println("1. List products");
            System.out.println("2. Add product");
            System.out.println("3. Update product stock");
            System.out.println("4. List users");
            System.out.println("5. Add user");
            System.out.println("6. View all orders");
            System.out.println("7. View receipt by order ID");
            System.out.println("8. Sales statistics");
            System.out.println("0. Logout");
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1 -> store.listProducts();
                case 2 -> addProduct();
                case 3 -> updateStock();
                case 4 -> store.listUsers();
                case 5 -> addUser();
                case 6 -> showOrders(store.getOrders());
                case 7 -> {
                    Order order = store.findOrderById(readInt("Order ID: "));
                    if (order == null) {
                        System.out.println("Order not found.");
                    } else {
                        order.printReceipt();
                    }
                }
                case 8 -> showStatistics();
                case 0 -> loggedIn = false;
                default -> System.out.println("Unknown option, try again.");
            }
        }
    }

    private void addProduct() {
        System.out.println("Product type: 1. Book   2. Electronics   3. Clothing");
        int type = readInt("Choose type: ");
        if (type < 1 || type > 3) {
            System.out.println("Unknown type.");
            return;
        }
        try {
            String name = readText("Name: ");
            double price = readDouble("Price (KZT): ");
            int stock = readInt("Stock quantity: ");
            Product product;
            if (type == 1) {
                String author = readText("Author: ");
                int pages = readInt("Pages: ");
                product = store.addBook(name, price, stock, author, pages);
            } else if (type == 2) {
                String brand = readText("Brand: ");
                int warranty = readInt("Warranty (months): ");
                product = store.addElectronics(name, price, stock, brand, warranty);
            } else {
                String size = readText("Size: ");
                String material = readText("Material: ");
                product = store.addClothing(name, price, stock, size, material);
            }
            System.out.println("Product added:");
            product.displayInfo();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateStock() {
        Product product = store.findProductById(readInt("Product ID: "));
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        try {
            product.setStockQuantity(readInt("New stock quantity: "));
            System.out.println("Stock updated:");
            product.displayInfo();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addUser() {
        System.out.println("User type: 1. Customer   2. Employee");
        int type = readInt("Choose type: ");
        if (type != 1 && type != 2) {
            System.out.println("Unknown type.");
            return;
        }
        try {
            String name = readText("Full name: ");
            String email = readText("Email: ");
            String password = readText("Password (min 4 chars): ");
            User user;
            if (type == 1) {
                String address = readText("Address: ");
                String phone = readText("Phone: ");
                user = store.addCustomer(name, email, password, address, phone);
            } else {
                String position = readText("Position: ");
                double salary = readDouble("Salary (KZT): ");
                user = store.addEmployee(name, email, password, position, salary);
            }
            System.out.println("User added:");
            user.displayInfo();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showStatistics() {
        System.out.println("Users    : " + store.getUsers().size());
        System.out.println("Products : " + store.getProducts().size());
        System.out.println("Orders   : " + store.getOrders().size());
        System.out.println("Items sold: " + store.getTotalItemsSold());
        System.out.printf("Revenue  : %.2f KZT%n", store.getTotalRevenue());
    }

    // ===================== SHARED HELPERS =====================

    private void showOrders(ArrayList<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders yet.");
            return;
        }
        for (Order order : orders) {
            order.displayInfo();
        }
    }

    private void printTitle(String title) {
        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println("  " + title);
        System.out.println("=".repeat(50));
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new InputClosedException();
        }
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            String input = readText(prompt).replace(',', '.');
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number, for example 1500 or 99.90");
            }
        }
    }

    /** Thrown when the console input ends (for example Ctrl+D). */
    public static class InputClosedException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
