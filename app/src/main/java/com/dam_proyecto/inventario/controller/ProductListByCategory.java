package com.dam_proyecto.inventario.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam_proyecto.inventario.R;
import com.dam_proyecto.inventario.adapter.ProductAdapter;
import com.dam_proyecto.inventario.model.Product;
import com.dam_proyecto.inventario.repository.ProductRepository;

import java.util.List;

public class ProductListByCategory extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView searchingText;
    RecyclerView productRecyclerView;
    List<Product> productList;
    ProductAdapter adapter;
    TextView title;
    Intent intent;
    Bundle extras;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        intent = this.getIntent();
        extras = intent.getExtras();
        category = extras.getString("category");

        title = findViewById(R.id.title);
        title.setText(category);

        searchingText = findViewById(R.id.searchingText);
        productRecyclerView = findViewById(R.id.productList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProductRepository productRepository = new ProductRepository(ProductListByCategory.this);
        productList = productRepository.showProductByCategory(category);

        adapter = new ProductAdapter(productList);
        productRecyclerView.setAdapter(adapter);

        DividerItemDecoration line = new DividerItemDecoration(
                this, new LinearLayoutManager(this).getOrientation());
        productRecyclerView.addItemDecoration(line);

        searchingText.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.searchingProduct(s);
        return false;
    }

    /* MENU 1 */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
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
            case R.id.menuAddNewProduct:
                goToActivity(NewProduct.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
