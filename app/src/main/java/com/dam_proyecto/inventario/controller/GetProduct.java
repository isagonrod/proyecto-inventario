package com.dam_proyecto.inventario.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dam_proyecto.inventario.R;
import com.dam_proyecto.inventario.model.Product;
import com.dam_proyecto.inventario.repository.ProductRepository;

public class GetProduct extends AppCompatActivity {

    TextView name, brand, amount, price, shop, category;
    Button btnSave, btnEdit, btnDelete;
    Product product;
    int id = 0;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_get);

        name = findViewById(R.id.nameTextView);
        brand = findViewById(R.id.brandTextView);
        amount = findViewById(R.id.amountTextView);
        price = findViewById(R.id.priceTextView);
        shop = findViewById(R.id.shopTextView);
        category = findViewById(R.id.categoryTextView);

        btnSave = findViewById(R.id.saveButton);
        btnEdit = findViewById(R.id.editButton);
        btnDelete = findViewById(R.id.deleteButton);

        btnSave.setVisibility(View.INVISIBLE);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        final ProductRepository productRepository = new ProductRepository(GetProduct.this);
        product = productRepository.getProduct(id);

        if (product != null) {
            name.setText(product.getName());
            brand.setText(product.getBrand());
            amount.setText(String.valueOf(product.getAmount()));
            price.setText(String.format("%.2f", product.getPrice()));
            shop.setText(product.getShop());
            category.setText(product.getCategory());

            btnSave.setVisibility(View.GONE);
        }

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(GetProduct.this, EditProduct.class);
            intent.putExtra("ID", id);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(GetProduct.this);
            builder.setTitle("ELIMINAR PRODUCTO");
            builder.setMessage("¿Desea eliminar este producto?")
                    .setPositiveButton("SÍ", (dialog, which) -> {
                        if (productRepository.deleteProduct(id)) {
                            goToActivity(ProductList.class);
                        }
                    })
                    .setNegativeButton("NO", (dialog, i) -> {

                    }).show();
        });
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
