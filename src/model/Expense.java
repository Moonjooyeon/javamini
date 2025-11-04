package model;

import java.time.LocalDate;

public class Expense extends Work {
    private int price;
    private String category;
    private LocalDate purchaseDate;

    public Expense(String title, String status, int price, String category, LocalDate purchaseDate) {
        super(title, status);
        this.price = price;
        this.category = category;
        this.purchaseDate = purchaseDate;
    }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
}
