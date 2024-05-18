package com.dam_proyecto.inventario.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dam_proyecto.inventario.model.Brand;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BrandRepository extends RepositoryHelper {

    Context context;
    RepositoryHelper helper;
    SQLiteDatabase db;

    public BrandRepository(@Nullable Context context) {
        super(context);
        this.context = context;
        this.helper = new RepositoryHelper(this.context);
        this.db = helper.getWritableDatabase();
    }

    @SuppressLint("Range")
    public List<Brand> showBrand() {
        List<Brand> brandList = new ArrayList<>();
        Brand brand;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_BRAND + " ORDER BY name ASC",
                null);

        if (cursor.moveToFirst()) {
            do {
                brand = new Brand();
                brand.setId(cursor.getInt(cursor.getColumnIndex("id")));
                brand.setName(cursor.getString(cursor.getColumnIndex("name")));
                brandList.add(brand);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return brandList;
    }

    public long addBrand(String name) {
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("name", name);
            id = db.insert(TABLE_BRAND, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }

    public boolean editBrand(int id, String name) {
        boolean correct;
        ContentValues values = new ContentValues();
        values.put("name", name);

        try {
            db.update(TABLE_BRAND, values, "id = " + id, null);
            correct = true;
        } catch (Exception ex) {
            ex.toString();
            correct = false;
        }

        return correct;
    }

    @SuppressLint("Range")
    public Brand getBrand(String name) {
        Brand brand = null;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_BRAND + " WHERE name = '" + name + "'",
                null);

        if (cursor.moveToFirst()) {
            brand = new Brand();
            brand.setId(cursor.getInt(cursor.getColumnIndex("id")));
            brand.setName(cursor.getString(cursor.getColumnIndex("name")));
        }

        cursor.close();
        return brand;
    }

}
