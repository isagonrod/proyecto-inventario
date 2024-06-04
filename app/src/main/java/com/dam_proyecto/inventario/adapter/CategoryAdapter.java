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

/**
 * Adapter for custom category list.
 *
 * @author Isabel María González Rodríguez
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    List<Category> categoryList;
    List<Category> originalCategoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
        originalCategoryList = new ArrayList<>();
        originalCategoryList.addAll(categoryList);
    }

    /**
     * Method where the personalized item of the category that will go in the category list is inserted.
     *
     * @param viewGroup - The ViewGroup into which the new View will be added after it is bound to
     *                    an adapter position.
     * @param i - The view type of the new View.
     *
     * @return - Custom view
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, null, true);
        return new CategoryViewHolder(view);
    }

    /**
     * Method to fill in the data of the custom category list.
     *
     * @param holder - The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position - The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.category.setText(categoryList.get(position).getName());
    }

    /**
     * Method to know the size of the category list.
     *
     * @return - Size of the category list
     */
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    /**
     * Class that controls the view of the screen where the category list is.
     */
    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        /**
         * Method where the personalized list of categories appears with all its declared elements
         * and the functions performed by the buttons that appears on it.
         *
         * @param itemView - Item view
         */
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
