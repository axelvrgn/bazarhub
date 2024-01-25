package com.example.bazarhub.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bazarhub.Models.Category;
import com.example.bazarhub.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Category> categories;
    private OnCategoryClickListener onCategoryClickListener;
    private String userRole;

    public void setUserRole(String role) {
        this.userRole = role;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.onCategoryClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category, categories, onCategoryClickListener, userRole);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageCategory;
        private TextView categoryName;
        private ImageView imageDelete;
        private ImageView imageEdit;
        private List<Category> categories;
        private OnCategoryClickListener listener;

        ViewHolder(View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.imageCategory);
            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            imageDelete = itemView.findViewById(R.id.imageDelete);
            imageEdit = itemView.findViewById(R.id.imageEdit);
        }

        void bind(Category category, List<Category> categories, OnCategoryClickListener listener, String userRole) {
            this.categories = categories;
            this.listener = listener;

            categoryName.setText(category.getName());

            Glide.with(itemView.getContext())
                    .load(category.getImage())
                    .into(imageCategory);

            if ("admin".equalsIgnoreCase(userRole)) {
                imageEdit.setVisibility(View.VISIBLE);
                imageDelete.setVisibility(View.VISIBLE);
            } else {
                imageEdit.setVisibility(View.GONE);
                imageDelete.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(categories.get(getAdapterPosition()));
                }
            });

            imageDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(category);
                }
            });

            imageEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(category);
                }
            });
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
        void onDeleteClick(Category category);
        void onEditClick(Category category);
    }
}
