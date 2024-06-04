package com.dam_proyecto.inventario.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.dam_proyecto.inventario.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that controls methods related to persistence in product databases.
 *
 * @author Isabel María González Rodríguez
 */
public class ProductRepository extends RepositoryHelper {

    Context context;
    RepositoryHelper helper;
    SQLiteDatabase db;

    public ProductRepository(@Nullable Context context) {
        super(context);
        this.context = context;
        this.helper = new RepositoryHelper(this.context);
        this.db = helper.getWritableDatabase();
    }

    /**
     * Method to add a product to the database.
     *
     * @param name - Product name
     * @param brand - Product brand
     * @param amount - Product amount
     * @param price - Product price
     * @param shop - Product shop
     * @param category - Product category
     * @param toBuy - Product if it is to buy
     * @return - Product id
     */
    public long addProduct(String name, String brand, String amount, String price,
                           String shop, String category, int toBuy) {
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("brand", brand);
            values.put("amount", amount);
            values.put("price", price);
            values.put("shop", shop);
            values.put("category", category);
            values.put("toBuy", toBuy);
            id = db.insert(TABLE_PRODUCT, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }

    /**
     * Method to list products from the database.
     *
     * @return - Product list
     */
    @SuppressLint("Range")
    public List<Product> showProduct() {
        List<Product> productList = new ArrayList<>();
        Product product;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_PRODUCT + " ORDER BY name ASC",
                null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex("id")));
                product.setName(cursor.getString(cursor.getColumnIndex("name")));
                product.setAmount(cursor.getInt(cursor.getColumnIndex("amount")));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                product.setShop(cursor.getString(cursor.getColumnIndex("shop")));
                product.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                product.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
                product.setToBuy(cursor.getInt(cursor.getColumnIndex("toBuy")));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    /**
     * Method to list products from the database by category.
     *
     * @param categoryName - Product category
     * @return - Product list
     */
    @SuppressLint("Range")
    public List<Product> showProductByCategory(String categoryName) {
        List<Product> productList = new ArrayList<>();
        Product product;
        String select = "SELECT * FROM " + TABLE_PRODUCT + " WHERE category = '" + categoryName + "' ORDER BY name ASC";
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex("id")));
                product.setName(cursor.getString(cursor.getColumnIndex("name")));
                product.setAmount(cursor.getInt(cursor.getColumnIndex("amount")));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                product.setShop(cursor.getString(cursor.getColumnIndex("shop")));
                product.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                product.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
                product.setToBuy(cursor.getInt(cursor.getColumnIndex("toBuy")));
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    /**
     * Method to list products to buy from the database.
     *
     * @return - Product list
     */
    @SuppressLint("Range")
    public List<Product> showProductToBuy() {
        List<Product> productList = new ArrayList<>();
        Product product;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_PRODUCT + " WHERE amount = 0 OR toBuy = 1 ORDER BY shop, name ASC",
                null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex("id")));
                product.setName(cursor.getString(cursor.getColumnIndex("name")));
                product.setAmount(cursor.getInt(cursor.getColumnIndex("amount")));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                product.setShop(cursor.getString(cursor.getColumnIndex("shop")));
                product.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                product.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
                product.setToBuy(cursor.getInt(cursor.getColumnIndex("toBuy")));
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    /**
     * Method to view data for a selected product.
     *
     * @param id - Product id
     * @return - Product object
     */
    @SuppressLint("Range")
    public Product getProduct(int id) {
        Product product = null;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_PRODUCT + " WHERE id = '" + id + "' LIMIT 1",
                null);

        if (cursor.moveToFirst()) {
            product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndex("id")));
            product.setName(cursor.getString(cursor.getColumnIndex("name")));
            product.setAmount(cursor.getInt(cursor.getColumnIndex("amount")));
            product.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            product.setShop(cursor.getString(cursor.getColumnIndex("shop")));
            product.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            product.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
        }

        cursor.close();
        return product;
    }

    /**
     * Method to edit the data of a selected product.
     *
     * @param id - Product id
     * @param name - Product name
     * @param brand - Product brand
     * @param amount - Product amount
     * @param price - Product price
     * @param shop - Product shop
     * @param category - Product category
     * @param toBuy - Product if it is to buy
     * @return - TRUE if the changes have been saved correctly
     */
    public boolean editProduct(int id, String name, String brand, int amount, double price,
                               String shop, String category, int toBuy) {
        boolean correct;

        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.execSQL("UPDATE " + TABLE_PRODUCT + " SET " +
                    "name = '" + name + "', " +
                    "brand = '" + brand + "', " +
                    "amount = '" + amount + "', " +
                    "price = '" + price + "', " +
                    "shop = '" + shop + "', " +
                    "category = '" + category + "', " +
                    "toBuy = '" + toBuy + "' " +
                    "WHERE id = '" + id + "'");
            correct = true;
        } catch (Exception ex) {
            ex.toString();
            correct = false;
        }

        return correct;
    }

    /**
     * Method to edit the product amount after end shopping.
     *
     * @param id - Product id
     * @param amount - New product amount
     */
    public void editProductAmount(int id, int amount) {
        db.execSQL("UPDATE " + TABLE_PRODUCT + " SET amount = '" + amount + "' WHERE id = '" + id + "'");
    }

    /**
     * Method to end shopping pressing a button.
     *
     * @param id - Product id
     * @param amount - New product amount
     * @return - TRUE if the changes have been saved correctly
     */
    public boolean endShopping(int id, int amount) {
        boolean correct;

        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.execSQL("UPDATE " + TABLE_PRODUCT + " SET amount = '" + amount + "', toBuy = '0' WHERE id = '" + id + "'");
            correct = true;
        } catch (Exception ex) {
            ex.toString();
            correct = false;
        }

        return correct;
    }

    /**
     * Method to delete a product from the database.
     *
     * @param id - Product id
     * @return - TRUE if the changes have been saved correctly
     */
    public boolean deleteProduct(int id) {
        boolean correct;

        try {
            db.execSQL("DELETE FROM " + TABLE_PRODUCT + " WHERE id = '" + id + "'");
            correct = true;
        } catch (Exception ex) {
            ex.toString();
            correct = false;
        }

        db.close();
        return correct;
    }
}
