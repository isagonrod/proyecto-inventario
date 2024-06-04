package com.dam_proyecto.inventario.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dam_proyecto.inventario.R;
import com.dam_proyecto.inventario.controller.EditProduct;
import com.dam_proyecto.inventario.controller.GetProduct;
import com.dam_proyecto.inventario.model.Product;
import com.dam_proyecto.inventario.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter for custom product list.
 *
 * @author Isabel María González Rodríguez
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    List<Product> productList;
    List<Product> originalProductList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        originalProductList = new ArrayList<>();
        originalProductList.addAll(productList);
    }

    /**
     * Method where the personalized item of the product that will go in the product list is inserted.
     *
     * @param viewGroup - The ViewGroup into which the new View will be added after it is bound to
     *                    an adapter position.
     * @param i - The view type of the new View.
     *
     * @return - Custom view
     */
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, null, false);
        return new ProductViewHolder(view);
    }

    /**
     * Method to fill in the data of the custom product item.
     *
     * @param holder - The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position - The position of the item within the adapter's data set.
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.name.setText(productList.get(position).getName());
        holder.amount.setText(String.valueOf(productList.get(position).getAmount()));
        holder.price.setText(String.format("%.2f", productList.get(position).getPrice()));
        holder.shop.setText(productList.get(position).getShop());
        holder.category.setText(productList.get(position).getCategory());
    }

    /**
     * Method to know the size of the product list.
     *
     * @return - Size of the product list
     */
    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * Method to search for products by name in the database.
     *
     * @param searchText - Keyword
     */
    @SuppressLint("NotifyDataSetChanged")
    public void searchingProduct(String searchText) {
        int length = searchText.length();
        if (length == 0) {
            productList.clear();
            productList.addAll(originalProductList);
        } else {
            List<Product> collection = productList.stream().filter(i -> i.getName().toLowerCase()
                    .contains(searchText.toLowerCase())).collect(Collectors.toList());
            productList.clear();
            productList.addAll(collection);
        }
        notifyDataSetChanged();
    }

    /**
     * Method to remove an item from the product list.
     *
     * @param position - Item position
     */
    public void deleteItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

    /**
     * Class that controls the view of the screen where the product list is.
     */
    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name, amount, price, shop, category;
        Button btnToBuy, btnEdit, btnDelete;
        ProductRepository productRepository;

        /**
         * Method where the personalized list of products appears with all its declared elements
         * and the functions performed by the buttons that appear on it.
         *
         * @param itemView - Item view
         */
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameItem);
            amount = itemView.findViewById(R.id.amountItem);
            price = itemView.findViewById(R.id.priceItem);
            shop = itemView.findViewById(R.id.shopItem);
            category = itemView.findViewById(R.id.categoryItem);

            btnToBuy = itemView.findViewById(R.id.shoppingCartButton);
            btnEdit = itemView.findViewById(R.id.editButton);
            btnDelete = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(view -> {
                decreaseAmount(getAdapterPosition());
            });

            itemView.setOnLongClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, GetProduct.class);
                intent.putExtra("ID", productList.get(getAdapterPosition()).getId());
                context.startActivity(intent);
                return true;
            });

            btnToBuy.setOnClickListener(view -> {
                productRepository = new ProductRepository(btnToBuy.getContext());
                productRepository.editProduct(
                        productList.get(getAdapterPosition()).getId(),
                        productList.get(getAdapterPosition()).getName(),
                        productList.get(getAdapterPosition()).getBrand(),
                        productList.get(getAdapterPosition()).getAmount(),
                        productList.get(getAdapterPosition()).getPrice(),
                        productList.get(getAdapterPosition()).getShop(),
                        productList.get(getAdapterPosition()).getCategory(),
                        1
                );
                Toast.makeText(btnToBuy.getContext(),
                        productList.get(getAdapterPosition()).getName() + "\nañadido a Lista de la Compra",
                        Toast.LENGTH_LONG).show();
            });

            btnEdit.setOnClickListener(view -> {
                Intent intent = new Intent(btnEdit.getContext(), EditProduct.class);
                intent.putExtra("ID", productList.get(getAdapterPosition()).getId());
                ContextCompat.startActivity(btnEdit.getContext(), intent, null);
            });

            btnDelete.setOnClickListener(view -> {
                productRepository = new ProductRepository(btnDelete.getContext());
                productRepository.deleteProduct(productList.get(getAdapterPosition()).getId());
                deleteItem(getAdapterPosition());
            });
        }

        /**
         * Method to reduce the amount of the product by clicking on it.
         *
         * @param position - Product position in the list
         */
        private void decreaseAmount(int position) {
            Product product = productList.get(position);
            int amount = productList.get(position).getAmount();

            if (amount > 0) {
                int result = amount - 1;
                product.setAmount(result);
                productList.set(position, product);
                notifyDataSetChanged();
                productRepository = new ProductRepository(itemView.getContext());
                productRepository.editProductAmount(productList.get(position).getId(), result);
            }
        }
    }
}
