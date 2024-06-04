package com.dam_proyecto.inventario.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.dam_proyecto.inventario.model.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that controls methods related to persistence in shop databases.
 *
 * @author Isabel María González Rodríguez
 */
public class ShopRepository extends RepositoryHelper {

    Context context;
    RepositoryHelper helper;
    SQLiteDatabase db;

    public ShopRepository(@Nullable Context context) {
        super(context);
        this.context = context;
        this.helper = new RepositoryHelper(this.context);
        this.db = helper.getWritableDatabase();
    }

    /**
     * Method to list shops from the database.
     *
     * @return - Shop list
     */
    @SuppressLint("Range")
    public List<Shop> showShop() {
        List<Shop> shopList = new ArrayList<>();
        Shop shop;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_SHOP + " ORDER BY name ASC",
                null);

        if (cursor.moveToFirst()) {
            do {
                shop = new Shop();
                shop.setId(cursor.getInt(cursor.getColumnIndex("id")));
                shop.setName(cursor.getString(cursor.getColumnIndex("name")));
                shopList.add(shop);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return shopList;
    }

    /**
     * Method to add a shop to the database.
     *
     * @param name - Shop name
     * @return - Shop id
     */
    public long addShop(String name) {
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("name", name);
            id = db.insert(TABLE_SHOP, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }

    /**
     * Method to edit the data of a selected shop.
     *
     * @param id - Shop id
     * @param name - Shop name
     * @return - TRUE if the changes have been saved correctly
     */
    public boolean editShop(int id, String name) {
        boolean correct;
        ContentValues values = new ContentValues();
        values.put("name", name);

        try {
            db.update(TABLE_SHOP, values, "id = " + id, null);
            correct = true;
        } catch (Exception ex) {
            ex.toString();
            correct = false;
        }

        return correct;
    }

    /**
     * Method to get a shop by name.
     *
     * @param name - Shop name
     * @return - Shop object
     */
    @SuppressLint("Range")
    public Shop getShop(String name) {
        Shop shop = null;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_SHOP + " WHERE name = '" + name + "'",
                null);

        if (cursor.moveToFirst()) {
            shop = new Shop();
            shop.setId(cursor.getInt(cursor.getColumnIndex("id")));
            shop.setName(cursor.getString(cursor.getColumnIndex("name")));
        }

        cursor.close();
        return shop;
    }
}
