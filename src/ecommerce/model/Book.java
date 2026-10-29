package ecommerce.model;

public class Book extends Product {

    private String author;
    private int pages;

    public Book(int id, String name, double price, int stockQuantity,
                String author, int pages) {
        super(id, name, price, stockQuantity);
        this.author = author;
        setPages(pages);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("Pages must be greater than 0.");
        }
        this.pages = pages;
    }

    @Override
    public String getCategory() {
        return "Book";
    }

    @Override
    protected String getDetails() {
        return "author: " + author + ", pages: " + pages;
    }
}
