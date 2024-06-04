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

/**
 * Class that controls methods related to persistence in category databases.
 *
 * @author Isabel María González Rodríguez
 */
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

    /**
     * Method to list categories from the database.
     *
     * @return - Category list
     */
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

    /**
     * Method to add a category to the database.
     *
     * @param name - Category name
     * @return - Category id
     */
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

    /**
     * Method to edit the data of a selected category.
     *
     * @param id - Category id
     * @param name - Category name
     * @return - TRUE if the changes have been saved correctly
     */
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

    /**
     * Method to get a category by name.
     *
     * @param name - Category name
     * @return - Category object
     */
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

    /**
     * Method to get a category by id.
     *
     * @param id - Category id
     * @return - Category object
     */
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

    /**
     * Method to delete a category from the database.
     *
     * @param categoryId - Category id
     */
    public void deleteCategory(int categoryId) {
        db.execSQL("DELETE FROM " + TABLE_CATEGORY + " WHERE id = '" + categoryId + "'");
    }

}
