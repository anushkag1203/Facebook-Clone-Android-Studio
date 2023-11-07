package com.example.androidproject.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.Comment;
import com.example.androidproject.CommentActivity;
import com.example.androidproject.Model.Notification;
import com.example.androidproject.R;
import com.example.androidproject.User;
import com.example.androidproject.databinding.NotificationRvDesignBinding;
import com.example.androidproject.databinding.NotificationRvDesignBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{

    ArrayList<Notification> list;
    Context context;

    public NotificationAdapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.notification_rv_design,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Notification notification=list.get(position);

        String text = TimeAgo.using(notification.getNotificationAt());

        String type=notification.getType();
        holder.binding.time.setText(text);

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile_image())
                                .placeholder(R.drawable.profile)
                                .into(holder.binding.friendImage);

                        if(type.equals("Like")){
                            holder.binding.notification1.setText(user.getFirstName()+" Liked your post");
                        }else if(type.equals("Comment")){
                            holder.binding.notification1.setText(user.getFirstName() +" Commented on your post");
                        }else {
                            holder.binding.notification1.setText(user.getFirstName() +" Start following you");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!type.equals("follow")) {

                    FirebaseDatabase.getInstance().getReference()
                                    .child("notification")
                                    .child(notification.getPostedBy())
                                    .child(notification.getNotificationId())
                            .child("checkOpen").setValue(true);

                    holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("postId", notification.getPostId());
                    intent.putExtra("postedBy", notification.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });

        Boolean checkOpen=notification.isCheckOpen();
        if ((checkOpen==true)){
            holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

       NotificationRvDesignBinding binding;
        public viewHolder(@NonNull View itemView){
            super(itemView);
            binding=NotificationRvDesignBinding.bind(itemView);
        }
    }


}