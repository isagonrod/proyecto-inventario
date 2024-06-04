package com.dam_proyecto.inventario.controller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam_proyecto.inventario.R;
import com.dam_proyecto.inventario.adapter.ShoppingAdapter;
import com.dam_proyecto.inventario.model.Product;
import com.dam_proyecto.inventario.repository.ProductRepository;

import java.util.List;

/**
 * Activity related to the function of displaying the complete shopping list.
 * Implements the custom list view, using the shopping adapter.
 *
 * @author Isabel María González Rodríguez
 */
public class ShoppingList extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView productList;
    List<Product> shoppingList;
    ShoppingAdapter adapter;
    SearchView searchText;
    Button finishShoppingButton;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);

        searchText = findViewById(R.id.searchingText);
        finishShoppingButton = findViewById(R.id.finishShoppingButton);
        productList = findViewById(R.id.productToBuyList);
        productList.setLayoutManager(new LinearLayoutManager(this));

        ProductRepository productRepository = new ProductRepository(ShoppingList.this);
        shoppingList = productRepository.showProductToBuy();
        adapter = new ShoppingAdapter(shoppingList);
        productList.setAdapter(adapter);

        DividerItemDecoration line = new DividerItemDecoration(
                this, new LinearLayoutManager(this).getOrientation());
        productList.addItemDecoration(line);

        searchText.setOnQueryTextListener(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = -1;
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        finishShoppingButton.setOnClickListener(v -> {
            for (int i = 0; i < productList.getChildCount(); i++) {
                View listItem = productList.getChildAt(i);
                int itemColor = listItem.getBackground() != null ?
                        ((ColorDrawable) listItem.getBackground()).getColor() : Color.WHITE;
                if (itemColor == Color.YELLOW) {
                    ProductRepository dbProduct = new ProductRepository(ShoppingList.this);
                    int amountBought = this.shoppingList.get(i).getAmount();
                    int startingAmount = dbProduct.getProduct(shoppingList.get(i).getId()).getAmount();
                    int totalAmount = startingAmount + amountBought;
                    dbProduct.endShopping(this.shoppingList.get(i).getId(), totalAmount);
                    adapter.deleteItem(i);
                    i--;
                    productList.removeView(listItem);
                } else {
                    Toast.makeText(
                            ShoppingList.this,
                            "Pulsa sobre el producto comprado\n antes de pulsar sobre el botón",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    /**
     * Method to search a product by shop.
     *
     * @param s - Keyword
     * @return - List of products in shopping list related to the keyword
     */
    @Override
    public boolean onQueryTextChange(String s) {
        adapter.shoppingListByShop(s);
        return false;
    }

    /* MENU 3 */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu3, menu);
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
