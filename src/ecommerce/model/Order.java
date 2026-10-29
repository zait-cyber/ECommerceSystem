package ecommerce.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * An order is made by one customer and contains one or more products.
 */
public class Order {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private int orderId;
    private Customer customer;
    private ArrayList<OrderItem> items;
    private double totalAmount;
    private LocalDateTime orderDate;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalAmount = 0;
        this.orderDate = LocalDateTime.now();
    }

    // ---------- getters ----------

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    /** Returns a copy, so nobody can change the order's list from outside. */
    public ArrayList<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // ---------- behaviour ----------

    /**
     * Adds a product to the order. If the product is already in the order,
     * only its quantity is increased.
     */
    public void addProduct(Product product, int quantity) {
        for (OrderItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                calculateTotal();
                return;
            }
        }
        items.add(new OrderItem(product, quantity));
        calculateTotal();
    }

    /** Recalculates totalAmount from all items. */
    public double calculateTotal() {
        double sum = 0;
        for (OrderItem item : items) {
            sum += item.getSubtotal();
        }
        totalAmount = sum;
        return totalAmount;
    }

    /** Short one-line description used in order lists. */
    public void displayInfo() {
        System.out.printf("Order #%-4d %s  customer: %-18s items: %-3d total: %.2f KZT%n",
                orderId, orderDate.format(DATE_FORMAT), customer.getName(),
                items.size(), totalAmount);
    }

    /** Prints a full receipt. */
    public void printReceipt() {
        String line = "-".repeat(62);
        System.out.println(line);
        System.out.println("                         RECEIPT");
        System.out.println(line);
        System.out.println("Order ID : " + orderId);
        System.out.println("Date     : " + orderDate.format(DATE_FORMAT));
        System.out.println("Customer : " + customer.getName() + " (" + customer.getEmail() + ")");
        System.out.println("Address  : " + customer.getAddress());
        System.out.println(line);
        System.out.printf("%-28s %5s %12s %13s%n", "Product", "Qty", "Price", "Subtotal");
        for (OrderItem item : items) {
            System.out.printf("%-28s %5d %12.2f %13.2f%n",
                    item.getProduct().getName(), item.getQuantity(),
                    item.getUnitPrice(), item.getSubtotal());
        }
        System.out.println(line);
        System.out.printf("%-47s %13.2f%n", "TOTAL (KZT)", totalAmount);
        System.out.println(line);
    }
}
