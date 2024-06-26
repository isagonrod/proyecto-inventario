package com.dam_proyecto.inventario.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam_proyecto.inventario.R;
import com.dam_proyecto.inventario.adapter.ProductAdapter;
import com.dam_proyecto.inventario.model.Product;
import com.dam_proyecto.inventario.repository.ProductRepository;

import java.util.List;

/**
 * Activity related to the function of displaying the complete list of products.
 * Implements the custom list view, using the product adapter.
 *
 * @author Isabel María González Rodríguez
 */
public class ProductList extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView searchText;
    RecyclerView productRecyclerView;
    List<Product> productList;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        searchText = findViewById(R.id.searchingText);
        productRecyclerView = findViewById(R.id.productList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProductRepository productRepository = new ProductRepository(ProductList.this);
        productList = productRepository.showProduct();
        adapter = new ProductAdapter(productList);
        productRecyclerView.setAdapter(adapter);

        DividerItemDecoration line = new DividerItemDecoration(
                this, new LinearLayoutManager(this).getOrientation());
        productRecyclerView.addItemDecoration(line);

        searchText.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    /**
     * Method to search a product by name.
     *
     * @param s - Keyword
     * @return - List of products related to the keyword
     */
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
