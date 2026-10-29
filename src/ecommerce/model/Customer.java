package ecommerce.model;

/**
 * A customer can browse products and place orders.
 */
public class Customer extends User {

    private String address;
    private String phone;

    public Customer(int id, String name, String email, String password,
                    String address, String phone) {
        super(id, name, email, password, Role.CUSTOMER);
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void displayInfo() {
        System.out.println(basicInfo() + " | address: " + address + " | phone: " + phone);
    }
}
