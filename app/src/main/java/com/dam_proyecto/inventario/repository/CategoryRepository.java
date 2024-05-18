package com.dam_proyecto.inventario.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dam_proyecto.inventario.model.Category;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository extends RepositoryHelper {

    Context context;
    RepositoryHelper helper;
    SQLiteDatabase db;

    public CategoryRepository(@Nullable Context context) {
        super(context);
        this.context = context;
        this.helper = new RepositoryHelper(this.context);
        this.db = helper.getWritableDatabase();
    }

    public List<Category> showCategory() {
        List<Category> categoryList = new ArrayList<>();
        Category category;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_CATEGORY + " ORDER BY name ASC",
                null);

        if (cursor.moveToFirst()) {
            do {
                category = new Category();
                category.setId(cursor.getInt(0));
                category.setName(cursor.getString(1));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return categoryList;
    }

    public long addCategory(String name) {
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("name", name);
            id = db.insert(TABLE_CATEGORY, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }

    public boolean editCategory(int id, String name) {
        boolean correct;

        ContentValues values = new ContentValues();
        values.put("name", name);

        try {
            db.update(TABLE_CATEGORY, values, "id = " + id, null);
            correct = true;
        } catch (Exception ex) {
            ex.toString();
            correct = false;
        }

        return correct;
    }

    @SuppressLint("Range")
    public Category getCategoryByName(String name) {
        Category category = null;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_CATEGORY + " WHERE name = '" + name + "'",
                null);

        if (cursor.moveToFirst()) {
            category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex("id")));
            category.setName(cursor.getString(cursor.getColumnIndex("name")));
        }

        cursor.close();
        return category;
    }

    @SuppressLint("Range")
    public Category getCategoryById(int id) {
        Category category = null;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_CATEGORY + " WHERE id = '" + id + "'",
                null);

        if (cursor.moveToFirst()) {
            category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex("id")));
            category.setName(cursor.getString(cursor.getColumnIndex("name")));
        }

        cursor.close();
        return category;
    }

    public boolean deleteCategory(String categoryName) {
        boolean correct;

        try {
            db.execSQL("DELETE FROM " + TABLE_CATEGORY + " WHERE name = '" + categoryName + "'");
            correct = true;
        } catch (Exception ex) {
            ex.toString();
            correct = false;
        }

        return correct;
    }

    public void deleteCategory(int categoryId) {
        db.execSQL("DELETE FROM " + TABLE_CATEGORY + " WHERE id = '" + categoryId + "'");
    }

}
