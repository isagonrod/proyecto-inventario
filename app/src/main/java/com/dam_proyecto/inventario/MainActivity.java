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

public class MainActivity extends AppCompatActivity {

    RecyclerView categories;
    List<Category> categoryList;
    CategoryAdapter adapter;
    FloatingActionButton btnNewProduct;

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

    /* MENU 2 */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

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

    private void goToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}