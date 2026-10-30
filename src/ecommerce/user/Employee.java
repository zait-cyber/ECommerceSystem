package ecommerce.user;

public class Employee extends User {
    private String position;

    public Employee(int id, String name, String email, String password, String position) {
        super(id, name, email, password, "employee");
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("    Position: " + position);
    }
}
