package ecommerce.model;

public class Electronics extends Product {

    private String brand;
    private int warrantyMonths;

    public Electronics(int id, String name, double price, int stockQuantity,
                       String brand, int warrantyMonths) {
        super(id, name, price, stockQuantity);
        this.brand = brand;
        setWarrantyMonths(warrantyMonths);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Warranty cannot be negative.");
        }
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }

    @Override
    protected String getDetails() {
        return "brand: " + brand + ", warranty: " + warrantyMonths + " months";
    }
}
