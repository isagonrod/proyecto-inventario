package com.dam_proyecto.inventario.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.Nullable;

/**
 * Class where the database is created (in the constructor)
 * and the tables (with SQL statements in the onCreate method)
 * that it will contain.
 *
 * @author Isabel María González Rodríguez
 */
public class RepositoryHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "inventario.db";

    public static final String TABLE_PRODUCT = "productos";
    public static final String TABLE_SHOP = "tiendas";
    public static final String TABLE_BRAND = "marcas";
    public static final String TABLE_CATEGORY = "categorias";

    /**
     * Constructor where the database is created and its version is controlled.
     *
     * @param context
     */
    public RepositoryHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Main method where the tables contained in the database are created with SQL statements.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_SHOP + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_BRAND + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_PRODUCT + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "brand INTEGER, " +
                "amount INTEGER NOT NULL, " +
                "price REAL, " +
                "shop INTEGER, " +
                "category INTEGER, " +
                "toBuy INTEGER, " +
                "FOREIGN KEY(shop) REFERENCES " + TABLE_SHOP + "(id), " +
                "FOREIGN KEY(category) REFERENCES " + TABLE_CATEGORY + "(id), " +
                "FOREIGN KEY(brand) REFERENCES " + TABLE_BRAND + "(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
