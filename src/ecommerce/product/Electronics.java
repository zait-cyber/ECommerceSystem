package ecommerce.product;

public class Electronics extends Product {
    private String brand;

    public Electronics(int id, String name, double price, int stockQuantity, String brand) {
        super(id, name, price, stockQuantity);
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("    Electronics, brand: " + brand);
    }
}
