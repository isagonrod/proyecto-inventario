package com.dam_proyecto.inventario.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dam_proyecto.inventario.R;

public class AppOperation extends AppCompatActivity {

    TextView operationTitle, operationText;
    String text =
            "1. En la pantalla principal aparecen las categorías de los productos.\n\n" +
            "2. Pulsando en cada categoría, aparecerán los productos asociados a estas.\n\n" +
            "3. Existe la opción de ver la lista completa de productos.\n\n" +
            "4. En cada producto (vista de la lista), está la opción de mandarlo a la lista de la " +
                    "compra (pulsando el carrito), editar sus datos (pulsando el lápiz), eliminarlo " +
                    "(pulsando la papelera), ver todos sus datos (dejando pulsado el dedo sobre él) " +
                    "o bien restar una unidad a la cantidad existente (pulsando una vez sobre él).\n\n" +
            "5. En la vista de los datos de un producto concreto, también lo podemos modificar " +
                    "(pulsando sobre el lápiz) o eliminarlo (pulsando la papelera).\n\n" +
            "6. En la vista de edición de un producto, una vez realizados los cambios, hay que pulsar" +
                    " sobre el icono del disquete para guardar los cambios.\n\n" +
            "7. En la pantalla de la lista de la compra, a la que se accede a través del icono del " +
                    "carrito de la compra, aparecerá un buscador para distinguir los productos por " +
                    "tienda, un botón que pulsaremos cuando hayamos terminado de comprar y la lista " +
                    "de productos para comprar.\n\n";

    @SuppressLint("MissingInfratedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_operation);
        operationTitle = findViewById(R.id.operationTitle);
        operationText = findViewById(R.id.operationText);
        operationText.setText(text);
    }

    /* MENU 3 */

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
