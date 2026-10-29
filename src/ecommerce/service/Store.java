package ecommerce.service;

import ecommerce.model.Book;
import ecommerce.model.Clothing;
import ecommerce.model.Customer;
import ecommerce.model.Electronics;
import ecommerce.model.Employee;
import ecommerce.model.Order;
import ecommerce.model.OrderItem;
import ecommerce.model.Product;
import ecommerce.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Store keeps all data of the system in ArrayLists
 * and contains the business logic (adding, searching, placing orders).
 * The console menus only call methods of this class.
 */
public class Store {

    private ArrayList<User> users;
    private ArrayList<Product> products;
    private ArrayList<Order> orders;

    private int nextUserId;
    private int nextProductId;
    private int nextOrderId;

    public Store() {
        users = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        nextUserId = 1;
        nextProductId = 1;
        nextOrderId = 1001;
    }

    // ===================== USERS =====================

    public Customer addCustomer(String name, String email, String password,
                                String address, String phone) {
        checkEmailIsFree(email);
        Customer customer = new Customer(nextUserId, name, email, password, address, phone);
        users.add(customer);
        nextUserId++;
        return customer;
    }

    public Employee addEmployee(String name, String email, String password,
                                String position, double salary) {
        checkEmailIsFree(email);
        Employee employee = new Employee(nextUserId, name, email, password, position, salary);
        users.add(employee);
        nextUserId++;
        return employee;
    }

    private void checkEmailIsFree(String email) {
        if (findUserByEmail(email) != null) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }
    }

    public User findUserByEmail(String email) {
        if (email == null) {
            return null;
        }
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }
        return null;
    }

    /** Returns the user if email and password are correct, otherwise null. */
    public User login(String email, String password) {
        User user = findUserByEmail(email);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void listUsers() {
        if (users.isEmpty()) {
            System.out.println("No users yet.");
            return;
        }
        for (User user : users) {
            user.displayInfo();   // polymorphism: Customer or Employee version is called
        }
    }

    // ===================== PRODUCTS =====================

    public Book addBook(String name, double price, int stock, String author, int pages) {
        Book book = new Book(nextProductId, name, price, stock, author, pages);
        products.add(book);
        nextProductId++;
        return book;
    }

    public Electronics addElectronics(String name, double price, int stock,
                                      String brand, int warrantyMonths) {
        Electronics item = new Electronics(nextProductId, name, price, stock, brand, warrantyMonths);
        products.add(item);
        nextProductId++;
        return item;
    }

    public Clothing addClothing(String name, double price, int stock,
                                String size, String material) {
        Clothing item = new Clothing(nextProductId, name, price, stock, size, material);
        products.add(item);
        nextProductId++;
        return item;
    }

    public Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void listProducts() {
        if (products.isEmpty()) {
            System.out.println("No products yet.");
            return;
        }
        for (Product product : products) {
            product.displayInfo();   // polymorphism: each subclass adds its own details
        }
    }

    // ===================== ORDERS =====================

    /**
     * Creates an order for the customer from a "cart"
     * (product -> quantity). Stock is checked for all products first,
     * and only then reduced, so a failed order changes nothing.
     */
    public Order placeOrder(Customer customer, Map<Product, Integer> cart) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            if (!entry.getKey().isInStock(entry.getValue())) {
                throw new IllegalArgumentException(
                        "Not enough stock for " + entry.getKey().getName() + ".");
            }
        }

        Order order = new Order(nextOrderId, customer);
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            entry.getKey().reduceStock(entry.getValue());
            order.addProduct(entry.getKey(), entry.getValue());
        }
        orders.add(order);
        nextOrderId++;
        return order;
    }

    public Order findOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    public ArrayList<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public ArrayList<Order> getOrdersOf(Customer customer) {
        ArrayList<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getId() == customer.getId()) {
                result.add(order);
            }
        }
        return result;
    }

    public double getTotalRevenue() {
        double sum = 0;
        for (Order order : orders) {
            sum += order.getTotalAmount();
        }
        return sum;
    }

    // ===================== DEMO DATA =====================

    /** Fills the store with sample data so the program can be demonstrated at once. */
    public void loadSampleData() {
        addEmployee("Admin", "admin@shop.kz", "admin123", "Store Manager", 450000);
        Customer aruzhan = addCustomer("Aruzhan Sadykova", "aruzhan@mail.kz", "1234",
                "Almaty, Abay Ave 10", "+7 701 111 2233");
        addCustomer("Daniyar Omarov", "daniyar@mail.kz", "1234",
                "Astana, Kabanbay Batyr 5", "+7 702 333 4455");

        Book cleanCode = addBook("Clean Code", 12500, 10, "Robert C. Martin", 464);
        addBook("Head First Java", 15000, 7, "Kathy Sierra", 720);
        Electronics mouse = addElectronics("Wireless Mouse", 8900, 25, "Logitech", 12);
        addElectronics("Laptop IdeaPad 3", 289000, 4, "Lenovo", 24);
        addClothing("Hoodie", 14990, 15, "L", "Cotton");
        addClothing("Denim Jeans", 19990, 12, "M", "Denim");

        Map<Product, Integer> cart = new LinkedHashMap<>();
        cart.put(cleanCode, 1);
        cart.put(mouse, 2);
        placeOrder(aruzhan, cart);
    }

    /** Used only to show the total number of sold pieces in the statistics. */
    public int getTotalItemsSold() {
        int count = 0;
        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                count += item.getQuantity();
            }
        }
        return count;
    }
}
