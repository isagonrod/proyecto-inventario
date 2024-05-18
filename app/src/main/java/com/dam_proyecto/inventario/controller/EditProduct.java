package com.dam_proyecto.inventario.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dam_proyecto.inventario.R;
import com.dam_proyecto.inventario.model.Brand;
import com.dam_proyecto.inventario.model.Category;
import com.dam_proyecto.inventario.model.Product;
import com.dam_proyecto.inventario.model.Shop;
import com.dam_proyecto.inventario.repository.BrandRepository;
import com.dam_proyecto.inventario.repository.CategoryRepository;
import com.dam_proyecto.inventario.repository.ProductRepository;
import com.dam_proyecto.inventario.repository.ShopRepository;

import java.util.List;

public class EditProduct extends AppCompatActivity {

    EditText name, amount, price;
    AutoCompleteTextView brand, shop, category;
    Button btnSave, btnEdit, btnDelete;
    Product product;
    int id = 0;
    boolean correct = false;
    ProductRepository productRepository;
    ShopRepository shopRepository;
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;
    List<Shop> shopList;
    List<Category> categoryList;
    List<Brand> brandList;

    @SuppressLint({"MissingInflatedId", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);

        name = findViewById(R.id.nameEditText);
        brand = findViewById(R.id.brandAutocompleteTextView);
        amount = findViewById(R.id.amountEditText);
        price = findViewById(R.id.priceEditText);
        shop = findViewById(R.id.shopAutocompleteTextView);
        category = findViewById(R.id.categoryAutocompleteTextView);

        btnSave = findViewById(R.id.saveButton);
        btnEdit = findViewById(R.id.editButton);
        btnDelete = findViewById(R.id.deleteButton);

        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);

        productRepository = new ProductRepository(EditProduct.this);
        shopRepository = new ShopRepository(EditProduct.this);
        categoryRepository = new CategoryRepository(EditProduct.this);
        brandRepository = new BrandRepository(EditProduct.this);

        shopList = shopRepository.showShop();
        categoryList = categoryRepository.showCategory();
        brandList = brandRepository.showBrand();

        ArrayAdapter<Shop> shopArrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, shopList);
        shop.setAdapter(shopArrayAdapter);

        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, categoryList);
        category.setAdapter(categoryArrayAdapter);

        ArrayAdapter<Brand> brandArrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, brandList);
        brand.setAdapter(brandArrayAdapter);

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

        product = productRepository.getProduct(id);

        if (product != null) {
            name.setText(product.getName());
            brand.setText(product.getBrand());
            amount.setText(String.valueOf(product.getAmount()));
            price.setText(String.format("%.2f", product.getPrice()));
            shop.setText(product.getShop());
            category.setText(product.getCategory());
        }

        btnSave.setOnClickListener(v -> {
            if (!name.getText().toString().equals("") &&
                    !amount.getText().toString().equals("") &&
                    !shop.getText().toString().equals("")) {

                correct = productRepository.editProduct(
                        id,
                        name.getText().toString(),
                        brand.getText().toString(),
                        Integer.parseInt(amount.getText().toString()),
                        Double.parseDouble(price.getText().toString().replace(",", ".")),
                        shop.getText().toString(),
                        category.getText().toString(),
                        0);

                Shop sh = shopRepository.getShop(shop.getText().toString());
                Category cat = categoryRepository.getCategoryByName(category.getText().toString());
                Brand br = brandRepository.getBrand(brand.getText().toString());

                if (sh == null) {
                    shopRepository.addShop(shop.getText().toString());
                } else {
                    shopRepository.editShop(sh.getId(), sh.getName());
                }

                if (cat == null) {
                    categoryRepository.addCategory(category.getText().toString());
                } else {
                    categoryRepository.editCategory(cat.getId(), cat.getName());
                }

                if (br == null) {
                    brandRepository.addBrand(brand.getText().toString());
                } else {
                    brandRepository.editBrand(br.getId(), br.getName());
                }

                if (correct) {
                    Toast.makeText(EditProduct.this, "PRODUCTO MODIFICADO", Toast.LENGTH_LONG).show();
                    reloadView();
                } else {
                    Toast.makeText(EditProduct.this, "ERROR AL MODIFICAR PRODUCTO", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(EditProduct.this, "DEBE RELLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reloadView() {
        Intent intent = new Intent(this, GetProduct.class);
        intent.putExtra("ID", id);
        intent.putExtra("category", category.getText().toString());
        startActivity(intent);
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
