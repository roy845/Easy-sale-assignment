package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.myapplication.R;
import com.example.myapplication.interfaces.OnClickUserInterface;
import com.example.myapplication.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    List<User> usersList;
    private int rowIndex = -1;
    Context context;
    private OnClickUserInterface onClickUserInterface;

    public UserAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        onClickUserInterface = (OnClickUserInterface) parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(usersList != null){

        User user = usersList.get(position);

        if (rowIndex == position){
            holder.row_linearlayout.setBackground(ContextCompat.getDrawable(context,R.drawable.lines2));
            holder.firstNameTextView.setTextColor(Color.BLACK);
            holder.lastNameTextView.setTextColor(Color.BLACK);
            holder.emailTextView.setTextColor(Color.BLACK);
        } else {
            holder.row_linearlayout.setBackground(ContextCompat.getDrawable(context,R.drawable.lines));
            holder.firstNameTextView.setTextColor(Color.WHITE);
            holder.lastNameTextView.setTextColor(Color.WHITE);
            holder.emailTextView.setTextColor(Color.WHITE);
        }
        //Regular Click
        holder.row_linearlayout.setOnClickListener(v -> {
            rowIndex = position;
            notifyItemChanged(rowIndex);
            notifyDataSetChanged();
            onClickUserInterface.onClickUser(user);
        });

        holder.avatarImageView.setOnClickListener(v->{
            createDialog(user.getAvatar(),user);
        });


        try {
            holder.bindData(user.getFirst_name(), user.getLast_name(),user.getEmail(),user.getAvatar());
        } catch (IOException e) {
            Log.e("UsersAdapter", "Error binding user data", e);
        }
        }
    }

    @Override
    public int getItemCount() {
        return this.usersList !=null ? this.usersList.size() : 0;
    }

    private void createDialog(String imageUrl,User user){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_image_recycler_view);
        ImageView dialogImage = dialog.findViewById(R.id.dialogImageRecyclerView);
        ImageView infoIcon = dialog.findViewById(R.id.infoIconRecyclerView);

        infoIcon.setOnClickListener(v->{
            dialog.dismiss();
            onClickUserInterface.onClickUser(user);
        });

        RequestManager glideRequestManager;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                glideRequestManager = Glide.with(context.getApplicationContext()); // Fallback to application context
            } else {
                glideRequestManager = Glide.with(activity); // Use activity context if valid
            }
        } else {
            glideRequestManager = Glide.with(context.getApplicationContext()); // For non-activity context
        }

        glideRequestManager
                .load(imageUrl)
                .into(dialogImage);

        dialog.show();
    }

    public void setUsers(List<User> users) {
        this.usersList = users;
        notifyDataSetChanged();

//        if(usersList == null){
//            usersList = new ArrayList<>();
//        }
//
//           UsersDiffCallback diffCallback = new UsersDiffCallback(usersList, users);
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//
//            usersList.clear();
//            usersList.addAll(users);
//
//            diffResult.dispatchUpdatesTo(this);
    }

    public void removeUser(int position){
        usersList.remove(position);
        notifyItemRemoved(position);
    }

    public void addUser(User user,int position){
        usersList.add(position,user);
        notifyItemInserted(position);
    }

    public User getUser(int position) {
        return usersList.get(position);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        private final Context context;
        private final View userItem;
        private final ImageView avatarImageView;
        private final TextView firstNameTextView;
        private final TextView lastNameTextView;
        private final TextView emailTextView;
        private final LinearLayout row_linearlayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.userItem = itemView.findViewById(R.id.user_item);
            this.avatarImageView = itemView.findViewById(R.id.avatar);
            this.firstNameTextView = itemView.findViewById(R.id.first_name);
            this.lastNameTextView = itemView.findViewById(R.id.last_name);
            emailTextView = itemView.findViewById(R.id.email);
            row_linearlayout = itemView.findViewById(R.id.linearLayout);
        }

        public void bindData(String firstName, String lastName,String email,String avatarUrl) throws IOException {
            firstNameTextView.setText(firstName);
            lastNameTextView.setText(lastName);
            emailTextView.setText(email);

            Glide.with(context)
                    .load(Uri.parse(avatarUrl))
                    .circleCrop()
                    .into(avatarImageView);
        }
    }
}
