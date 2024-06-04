package com.dam_proyecto.inventario.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam_proyecto.inventario.R;
import com.dam_proyecto.inventario.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter for custom shopping list.
 *
 * @author Isabel María González Rodríguez
 */
public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder> {

    List<Product> productList;
    List<Product> originalProductList;

    public ShoppingAdapter(List<Product> productList) {
        this.productList = productList;
        originalProductList = new ArrayList<>();
        originalProductList.addAll(productList);
    }

    /**
     * Method where the personalized item of the shopping list that will go in the shopping list
     * is inserted.
     *
     * @param viewGroup - The ViewGroup into which the new View will be added after it is bound to
     *                    an adapter position.
     * @param i - The view type of the new View.
     *
     * @return - Custom view
     */
    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shopping_item, viewGroup, false);
        return new ShoppingViewHolder(view);
    }

    /**
     * Method to fill in the data of the custom shopping list.
     *
     * @param holder - The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position - The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        holder.nameTextView.setText(productList.get(position).getName());
        holder.brandTextView.setText(productList.get(position).getBrand());
        holder.categoryTextView.setText(productList.get(position).getCategory());
        holder.shopTextView.setText(productList.get(position).getShop());
    }

    /**
     * Method to search for products by shop in the database.
     *
     * @param shop - Keyword
     */
    @SuppressLint("NotifyDataSetChanged")
    public void shoppingListByShop(String shop) {
        int length = shop.length();
        if (length == 0) {
            productList.clear();
            productList.addAll(originalProductList);
        } else {
            List<Product> collection = productList.stream().filter(i -> i.getShop().toLowerCase()
                    .contains(shop.toLowerCase())).collect(Collectors.toList());
            productList.clear();
            productList.addAll(collection);
        }
        notifyDataSetChanged();
    }

    /**
     * Method to know the size of the shopping list.
     *
     * @return - Size of the shopping list
     */
    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * Method to get the item position in the shopping list by id.
     *
     * @param itemId - Product id
     * @return - Item position in shopping list
     */
    public int getPositionInListFromItemId(int itemId) {
        int originalItemPosition = -1;

        for(int i = 0; i < originalProductList.size(); i++) {
            int currentId = originalProductList.get(i).getId();
            if (currentId == itemId) {
                originalItemPosition = i;
            }
        }

        return originalItemPosition;
    }

    /**
     * Method to remove an item from the shopping list.
     *
     * @param position - Item position
     */
    public void deleteItem(int position) {
        int originalItemPosition = getPositionInListFromItemId(productList.get(position).getId());
        originalProductList.remove(originalItemPosition);
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

    /**
     * Class that controls the view of the screen where the shopping list is.
     */
    public class ShoppingViewHolder extends RecyclerView.ViewHolder {

        EditText amountEditText;
        TextView nameTextView, brandTextView, shopTextView, categoryTextView;

        /**
         * Method where the personalized shopping list of products appears with all its declared
         * elements and the functions performed by the buttons that appear on it.
         *
         * @param itemView - Item view
         */
        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);

            amountEditText = itemView.findViewById(R.id.amountEditText);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            brandTextView = itemView.findViewById(R.id.brandTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            shopTextView = itemView.findViewById(R.id.shopTextView);

            itemView.setOnClickListener(view -> {
                int itemColor = view.getBackground() != null ? ((ColorDrawable) view.getBackground())
                        .getColor() : Color.WHITE;
                if (itemColor == Color.WHITE) {
                    String amount = String.valueOf(amountEditText.getText());
                    if (amount.isEmpty()) {
                        Toast.makeText(itemView.getContext(),
                                "Por favor, introduce la cantidad comprada",
                                Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            productList.get(getAdapterPosition()).setAmount(Integer.parseInt(String.valueOf(amountEditText.getText())));
                            itemView.setBackgroundColor(Color.YELLOW);
                            productList.get(getAdapterPosition()).setToBuy(1);
                        } catch (NumberFormatException ex) {
                            Toast.makeText(itemView.getContext(),
                                    "Por favor, asegúrate de que la cantidad introducida es numérica y no demasiado grande.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    amountEditText.setText("");
                    itemView.setBackgroundColor(Color.WHITE);
                    productList.get(getAdapterPosition()).setToBuy(0);
                }

            });
        }
    }
}
