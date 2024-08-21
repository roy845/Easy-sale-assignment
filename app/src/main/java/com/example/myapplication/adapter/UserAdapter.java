package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.myapplication.R;
import com.example.myapplication.interfaces.OnClickUserInterface;
import com.example.myapplication.models.User;
import com.example.myapplication.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserAdapter extends PagingDataAdapter<User, UserAdapter.UserViewHolder> {

//    List<User> usersList;
//    private int rowIndex = -1;
      private final Context context;
    private OnClickUserInterface onClickUserInterface;

    public UserAdapter(@NonNull DiffUtil.ItemCallback<User> diffCallback,Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        onClickUserInterface = (OnClickUserInterface) parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    public User getUser(int position){
        return getItem(position);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = getItem(position);
        if(user != null){

//        if (rowIndex == position){
//            holder.row_linearlayout.setBackground(ContextCompat.getDrawable(context,R.drawable.lines2));
//            holder.firstNameTextView.setTextColor(Color.BLACK);
//            holder.lastNameTextView.setTextColor(Color.BLACK);
//            holder.emailTextView.setTextColor(Color.BLACK);
//        } else {
//            holder.row_linearlayout.setBackground(ContextCompat.getDrawable(context,R.drawable.lines));
//            holder.firstNameTextView.setTextColor(Color.WHITE);
//            holder.lastNameTextView.setTextColor(Color.WHITE);
//            holder.emailTextView.setTextColor(Color.WHITE);
//        }

        //Regular Click
        holder.row_linearlayout.setOnClickListener(v -> {
//            rowIndex = position;
//            notifyItemChanged(rowIndex);
//            notifyDataSetChanged();
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

    public static final DiffUtil.ItemCallback<User> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<User>() {
                @Override
                public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                    return oldItem.getId() == (newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                    return oldItem.equals(newItem);
                }
            };

//    @Override
//    public int getItemCount() {
//        return this.usersList !=null ? this.usersList.size() : 0;
//    }

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

//    public void setUsers(List<User> users) {
//        this.usersList = users;
//        notifyDataSetChanged();
//
////        if(usersList == null){
////            usersList = new ArrayList<>();
////        }
////
////           UsersDiffCallback diffCallback = new UsersDiffCallback(usersList, users);
////            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
////
////            usersList.clear();
////            usersList.addAll(users);
////
////            diffResult.dispatchUpdatesTo(this);
//    }

//    public void removeUser(int position) {
//
//        String userRemoved = usersList.remove(position).getFirst_name();
//        System.out.println("item removed:"+usersListFull.get(position).getFirst_name());
//
//        for(int i = 0 ; i<usersListFull.size();i++)
//        {
//            if(usersListFull.get(i).getFirst_name().equals(userRemoved)){
//                usersListFull.remove(i);
//            }
//        }
//
//        notifyItemChanged(position);
//        notifyDataSetChanged();
//
//
//        notifyItemRangeChanged(position, usersList.size());
//        userViewModel.setUsersLiveData(usersList);
//
//        if (position < rowIndex) {
//            rowIndex--;
//            notifyItemChanged(rowIndex);
//            notifyDataSetChanged();
//        } else if (position == rowIndex) {
//            rowIndex = -1;
//            notifyItemChanged(rowIndex);
//            notifyDataSetChanged();
//        }
//    }

//    public void addUser(User user,int position){
//        usersList.add(position, user);
//        usersListFull.add(position, user);
//        usersList.sort(new Comparator<User>() {
//            @Override
//            public int compare(User o1, User o2) {
//                return o1.getFirst_name().compareTo(o2.getFirst_name());
//
//            }
//        });
//
//        usersListFull.sort(new Comparator<User>() {
//            @Override
//            public int compare(User o1, User o2) {
//                return o1.getFirst_name().compareTo(o2.getFirst_name());
//
//            }
//        });
//
//        notifyDataSetChanged();
//    }
//
//    public User getUser(int position) {
//        return usersList.get(position);
//    }

//    private Filter exampleFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<User> filteredList = new ArrayList<>();
//            if(constraint == null || constraint.length() == 0) {
//                filteredList.addAll(usersList);
//            }
//            else{
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for(User user:usersList) {
//                    if (user.getFirst_name().toLowerCase().contains(filterPattern) ||
//                            user.getLast_name().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(user);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            if(usersList!=null) {
//                usersList.clear();
//                usersList.addAll((ArrayList<User>) results.values);
//                notifyDataSetChanged();
//
//            }
//        }
//    };
//
//    @Override
//    public Filter getFilter() {
//        return exampleFilter;
//    }

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
