package ecommerce.model;

/**
 * Base class for every person who can log in to the system.
 * It is abstract because a "plain" user does not exist:
 * every user is either a Customer or an Employee.
 */
public abstract class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;

    public User(int id, String name, String email, String password, Role role) {
        this.id = id;
        setName(name);
        setEmail(email);
        setPassword(password);
        this.role = role;
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
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'.");
        }
        this.email = email.trim().toLowerCase();
    }

    /**
     * There is no getPassword() on purpose: the password should never
     * be read from outside. Other classes can only check it.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must have at least 4 characters.");
        }
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    // ---------- behaviour ----------

    /** Every subclass prints its own extra details. */
    public abstract void displayInfo();

    /** Common part of displayInfo() reused by subclasses. */
    protected String basicInfo() {
        return String.format("#%d | %-18s | %-24s | %s", id, name, email, role);
    }
}
