package com.example.android.inventoryapp2;


public class Product {

    private String name;
    private int qty;
    private double price;
    private String supplier;
    private String email;
    private byte[] image;

    Product(String name, double price, int qty, String supplier_name, String email, byte[] image) {
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.supplier = supplier_name;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", supplier='" + supplier + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
