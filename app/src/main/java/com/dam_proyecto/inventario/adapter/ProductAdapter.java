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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    List<Product> productList;
    List<Product> originalProductList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        originalProductList = new ArrayList<>();
        originalProductList.addAll(productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, null, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.name.setText(productList.get(position).getName());
        holder.amount.setText(String.valueOf(productList.get(position).getAmount()));
        holder.price.setText(String.format("%.2f", productList.get(position).getPrice()));
        holder.shop.setText(productList.get(position).getShop());
        holder.category.setText(productList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

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

    public void deleteItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name, amount, price, shop, category;
        Button btnToBuy, btnEdit, btnDelete;
        ProductRepository productRepository;

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
