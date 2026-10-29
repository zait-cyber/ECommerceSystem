package ecommerce.model;

/**
 * Base class for everything that can be sold.
 * It is abstract because the store only sells concrete kinds of products
 * (Book, Electronics, Clothing).
 */
public abstract class Product {

    private int id;
    private String name;
    private double price;
    private int stockQuantity;

    public Product(int id, String name, double price, int stockQuantity) {
        this.id = id;
        setName(name);
        setPrice(price);
        setStockQuantity(stockQuantity);
    }

    // ---------- getters and setters ----------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        this.name = name.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }
        this.stockQuantity = stockQuantity;
    }

    // ---------- behaviour ----------

    public boolean isInStock(int quantity) {
        return quantity > 0 && quantity <= stockQuantity;
    }

    /** Called when an order is placed. */
    public void reduceStock(int quantity) {
        if (!isInStock(quantity)) {
            throw new IllegalArgumentException("Not enough stock for " + name + ".");
        }
        stockQuantity -= quantity;
    }

    /** Category name shown in lists, e.g. "Book". */
    public abstract String getCategory();

    /** Extra details that depend on the product type. */
    protected abstract String getDetails();

    /** Prints one line with all information about the product. */
    public void displayInfo() {
        System.out.printf("#%-3d %-14s %-22s %11.2f KZT  stock: %-4d %s%n",
                id, "[" + getCategory() + "]", name, price, stockQuantity, getDetails());
    }
}
