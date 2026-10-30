package ecommerce;

import ecommerce.order.Order;
import ecommerce.product.Book;
import ecommerce.product.Clothing;
import ecommerce.product.Electronics;
import ecommerce.product.Product;
import ecommerce.user.Customer;
import ecommerce.user.Employee;
import ecommerce.user.User;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // start data
        users.add(new Employee(1, "Admin", "admin@shop.kz", "admin123", "Manager"));
        users.add(new Customer(2, "Aruzhan", "aruzhan@mail.kz", "1234", "Almaty"));
        products.add(new Book(1, "Clean Code", 12500, 10, "Robert Martin"));
        products.add(new Electronics(2, "Wireless Mouse", 8900, 20, "Logitech"));
        products.add(new Clothing(3, "Hoodie", 14990, 15, "L"));

        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Login");
            System.out.println("2. List products");
            System.out.println("0. Exit");
            int choice = readNumber("Choose: ");

            if (choice == 1) {
                login();
            } else if (choice == 2) {
                listProducts();
            } else if (choice == 0) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Wrong choice!");
            }
        }
    }

    private static void login() {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                System.out.println("Welcome, " + u.getName() + "!");
                if (u.getRole().equals("customer")) {
                    customerMenu((Customer) u);
                } else {
                    employeeMenu();
                }
                return;
            }
        }
        System.out.println("Wrong email or password!");
    }

    // ---------------- CUSTOMER MENU ----------------
    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1. List products");
            System.out.println("2. Place order");
            System.out.println("3. My orders");
            System.out.println("0. Logout");
            int choice = readNumber("Choose: ");

            if (choice == 1) {
                listProducts();
            } else if (choice == 2) {
                placeOrder(customer);
            } else if (choice == 3) {
                for (Order o : orders) {
                    if (o.getCustomer() == customer) {
                        o.printReceipt();
                    }
                }
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Wrong choice!");
            }
        }
    }

    private static void placeOrder(Customer customer) {
        Order order = new Order(orders.size() + 1, customer);

        while (true) {
            listProducts();
            int id = readNumber("Enter product ID to add (0 = finish): ");
            if (id == 0) {
                break;
            }
            Product product = findProduct(id);
            if (product == null) {
                System.out.println("Product not found!");
            } else if (product.getStockQuantity() == 0) {
                System.out.println("Out of stock!");
            } else {
                order.addProduct(product);
                product.setStockQuantity(product.getStockQuantity() - 1);
                System.out.println(product.getName() + " added to order.");
            }
        }

        if (order.getProducts().isEmpty()) {
            System.out.println("Order is empty.");
        } else {
            orders.add(order);
            System.out.println("Order placed!");
            order.printReceipt();
        }
    }

    // ---------------- EMPLOYEE MENU ----------------
    private static void employeeMenu() {
        while (true) {
            System.out.println("\n===== EMPLOYEE MENU =====");
            System.out.println("1. List products");
            System.out.println("2. Add product");
            System.out.println("3. List users");
            System.out.println("4. Add user");
            System.out.println("5. View all orders");
            System.out.println("0. Logout");
            int choice = readNumber("Choose: ");

            if (choice == 1) {
                listProducts();
            } else if (choice == 2) {
                addProduct();
            } else if (choice == 3) {
                listUsers();
            } else if (choice == 4) {
                addUser();
            } else if (choice == 5) {
                if (orders.isEmpty()) {
                    System.out.println("No orders yet.");
                }
                for (Order o : orders) {
                    o.printReceipt();
                }
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Wrong choice!");
            }
        }
    }

    private static void addProduct() {
        System.out.println("Type: 1 - Book, 2 - Electronics, 3 - Clothing");
        int type = readNumber("Choose: ");
        System.out.print("Name: ");
        String name = sc.nextLine();
        int price = readNumber("Price: ");
        int stock = readNumber("Quantity: ");
        int id = products.size() + 1;

        if (type == 1) {
            System.out.print("Author: ");
            products.add(new Book(id, name, price, stock, sc.nextLine()));
        } else if (type == 2) {
            System.out.print("Brand: ");
            products.add(new Electronics(id, name, price, stock, sc.nextLine()));
        } else if (type == 3) {
            System.out.print("Size: ");
            products.add(new Clothing(id, name, price, stock, sc.nextLine()));
        } else {
            System.out.println("Wrong type!");
            return;
        }
        System.out.println("Product added!");
    }

    private static void addUser() {
        System.out.println("Type: 1 - Customer, 2 - Employee");
        int type = readNumber("Choose: ");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        int id = users.size() + 1;

        if (type == 1) {
            System.out.print("Address: ");
            users.add(new Customer(id, name, email, password, sc.nextLine()));
        } else if (type == 2) {
            System.out.print("Position: ");
            users.add(new Employee(id, name, email, password, sc.nextLine()));
        } else {
            System.out.println("Wrong type!");
            return;
        }
        System.out.println("User added!");
    }

    // ---------------- HELPERS ----------------
    private static void listProducts() {
        System.out.println("\n--- Products ---");
        for (Product p : products) {
            p.displayInfo();
        }
    }

    private static void listUsers() {
        System.out.println("\n--- Users ---");
        for (User u : users) {
            u.displayInfo();
        }
    }

    private static Product findProduct(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private static int readNumber(String text) {
        while (true) {
            System.out.print(text);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number!");
            }
        }
    }
}
