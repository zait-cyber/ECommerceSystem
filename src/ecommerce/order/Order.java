package ecommerce.order;

import ecommerce.product.Product;
import ecommerce.user.Customer;

import java.util.ArrayList;

public class Order {
    private int orderId;
    private Customer customer;
    private ArrayList<Product> products;
    private double totalAmount;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.products = new ArrayList<>();
        this.totalAmount = 0;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void addProduct(Product product) {
        products.add(product);
        totalAmount = totalAmount + product.getPrice();
    }

    public void printReceipt() {
        System.out.println("------------- RECEIPT -------------");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customer.getName());
        for (Product p : products) {
            System.out.println("  " + p.getName() + " - " + p.getPrice() + " KZT");
        }
        System.out.println("Total: " + totalAmount + " KZT");
        System.out.println("-----------------------------------");
    }
}
