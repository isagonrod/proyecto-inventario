package com.dam_proyecto.inventario.model;

/**
 * Product entity used for persistence as a database table.
 * It is related to the rest of the entities in the model.
 *
 * @author Isabel María González Rodríguez
 */
public class Product {

    private int id;
    private String name;
    private int amount;
    private double price;
    private String shop;
    private String category;
    private int toBuy;
    private String brand;

    public Product() {
        this.toBuy = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getToBuy() {
        return toBuy;
    }

    public void setToBuy(int toBuy) {
        this.toBuy = toBuy;
    }
}
