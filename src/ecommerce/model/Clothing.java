package ecommerce.model;

public class Clothing extends Product {

    private String size;
    private String material;

    public Clothing(int id, String name, double price, int stockQuantity,
                    String size, String material) {
        super(id, name, price, stockQuantity);
        this.size = size;
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String getCategory() {
        return "Clothing";
    }

    @Override
    protected String getDetails() {
        return "size: " + size + ", material: " + material;
    }
}
