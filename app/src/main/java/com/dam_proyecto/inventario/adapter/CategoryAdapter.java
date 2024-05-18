package com.dam_proyecto.inventario.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam_proyecto.inventario.R;
import com.dam_proyecto.inventario.controller.ProductList;
import com.dam_proyecto.inventario.controller.ProductListByCategory;
import com.dam_proyecto.inventario.model.Category;
import com.dam_proyecto.inventario.repository.CategoryRepository;
import com.dam_proyecto.inventario.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    List<Category> categoryList;
    List<Category> originalCategoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
        originalCategoryList = new ArrayList<>();
        originalCategoryList.addAll(categoryList);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, null, true);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.category.setText(categoryList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        @SuppressLint("NotifyDataSetChanged")
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.categoryItem);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), ProductListByCategory.class);
                intent.putExtra("category", categoryList.get(getAdapterPosition()).getName());
                view.getContext().startActivity(intent);
            });

            itemView.setOnLongClickListener(view -> {
                CategoryRepository categoryRepository = new CategoryRepository(view.getContext());
                ProductRepository productRepository = new ProductRepository(view.getContext());
                Category cat = categoryRepository.getCategoryById(categoryList.get(getAdapterPosition()).getId());
                if (cat != null) {
                    if (productRepository.showProductByCategory(categoryList.get(getAdapterPosition()).getName()).isEmpty()) {
                        categoryRepository.deleteCategory(cat.getId());
                        categoryList.remove(categoryList.get(getAdapterPosition()));
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), categoryList.size());
                        Toast.makeText(view.getContext(), "Categoría vacía borrada con éxito", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(view.getContext(), "No se ha podido eliminar la categoría porque tiene productos asociados", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "No se ha podido eliminar la categoría porque no existe en la base de datos", Toast.LENGTH_LONG).show();
                }
                return false;
            });
        }
    }
}
