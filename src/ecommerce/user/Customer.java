package ecommerce.user;

public class Customer extends User {
    private String address;

    public Customer(int id, String name, String email, String password, String address) {
        super(id, name, email, password, "customer");
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("    Address: " + address);
    }
}
