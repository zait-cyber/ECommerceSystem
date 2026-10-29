package ecommerce.model;

/**
 * An employee manages the store: adds products, users and views all orders.
 */
public class Employee extends User {

    private String position;
    private double salary;

    public Employee(int id, String name, String email, String password,
                    String position, double salary) {
        super(id, name, email, password, Role.EMPLOYEE);
        this.position = position;
        setSalary(salary);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        this.salary = salary;
    }

    @Override
    public void displayInfo() {
        System.out.println(basicInfo() + " | position: " + position
                + " | salary: " + String.format("%.2f", salary));
    }
}
