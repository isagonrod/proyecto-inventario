package com.dam_proyecto.inventario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam_proyecto.inventario.adapter.CategoryAdapter;
import com.dam_proyecto.inventario.controller.AppOperation;
import com.dam_proyecto.inventario.controller.NewProduct;
import com.dam_proyecto.inventario.controller.ProductList;
import com.dam_proyecto.inventario.controller.ShoppingList;
import com.dam_proyecto.inventario.model.Category;
import com.dam_proyecto.inventario.repository.CategoryRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Main screen in which a menu is displayed at the top,
 * a floating button to add new products to the inventory
 * and in the central part of the screen buttons related
 * to the categories of the products created will be created.
 *
 * @author Isabel María González Rodríguez
 */
public class MainActivity extends AppCompatActivity {

    RecyclerView categories;
    List<Category> categoryList;
    CategoryAdapter adapter;
    FloatingActionButton btnNewProduct;

    /**
     * Method to render the main screen elements.
     *
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categories = findViewById(R.id.categoryRecyclerView);
        categories.setLayoutManager(new GridLayoutManager(this, 2));

        CategoryRepository categoryRepository = new CategoryRepository(this);
        categoryList = categoryRepository.showCategory();
        adapter = new CategoryAdapter(categoryList);
        categories.setAdapter(adapter);

        btnNewProduct = findViewById(R.id.btn_new_product);
        btnNewProduct.setOnClickListener(v -> goToActivity(NewProduct.class));
    }

    /**
     * Method to create the top menu.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    /**
     * Method for creating top menu options and what they do when you click on them.
     *
     * @param item - Menu option pressed.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAppOperation:
                goToActivity(AppOperation.class);
                return true;
            case R.id.menuShoppingList:
                goToActivity(ShoppingList.class);
                return true;
            case R.id.menuProductList:
                goToActivity(ProductList.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Generic method to switch activity/screen.
     *
     * @param activity - Activity you want to change to.
     */
    private void goToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}