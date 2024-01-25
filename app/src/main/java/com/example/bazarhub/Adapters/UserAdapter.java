package com.example.bazarhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bazarhub.R;
import com.example.bazarhub.Models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> users;

    private String userRole;

    public void setUserRole(String role) {
        this.userRole = role;
    }
    private OnUserClickListener onUserClickListener;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    public void setOnUserClickListener(UserAdapter.OnUserClickListener listener) {
        this.onUserClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User u = users.get(position);
        Glide.with(context).load(u.getAvatar()).into(holder.ivAvatar);
        holder.tvName.setText(u.getName());
        holder.tvEmail.setText(u.getEmail());


        holder.bind(u, users, onUserClickListener, userRole);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvName, tvEmail;

        private OnUserClickListener listener;
        private List<User> users;

        private ImageView imageDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            imageDelete = itemView.findViewById(R.id.imageDelete);
        }


        void bind(User user, List<User> users, UserAdapter.OnUserClickListener listener, String userRole) {
            this.users = users;
            this.listener = listener;

            imageDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(user);
                }
            });

        }
    }
    public interface OnUserClickListener {
        void onDeleteClick(User user);

    }

}
