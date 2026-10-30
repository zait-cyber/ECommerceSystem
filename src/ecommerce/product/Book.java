package ecommerce.product;

public class Book extends Product {
    private String author;

    public Book(int id, String name, double price, int stockQuantity, String author) {
        super(id, name, price, stockQuantity);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("    Book, author: " + author);
    }
}
