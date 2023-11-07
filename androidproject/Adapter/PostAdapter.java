package com.example.androidproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.CommentActivity;
import com.example.androidproject.Model.Notification;
import com.example.androidproject.Model.Post;
import com.example.androidproject.R;
import com.example.androidproject.User;
import com.example.androidproject.databinding.DashbordRvSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{

    ArrayList<Post>list;
    Context context;

    public PostAdapter(ArrayList<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dashbord_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Post model=list.get(position);
        Picasso.get().load(model.getPosiImage())
                .placeholder(R.drawable.flower)
                .into(holder.binding.postImg);
                holder.binding.like.setText(model.getPostLike()+"");
                holder.binding.comment.setText(model.getCommentCount()+"");
        String descrption= model.getPostDescription();
        if (descrption.equals("")){
            holder.binding.postDesc.setVisibility(View.GONE);
        }else {
            holder.binding.postDesc.setText(model.getPostDescription());
            holder.binding.postDesc.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(model.getpostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile_image())
                                .placeholder(R.drawable.profile).into(holder.binding.friendImage);
                        holder.binding.userName.setText(user.getFirstName());
                        holder.binding.about.setText(user.getEmail());



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                        .child("posts")
                                .child(model.getPostId())
                                .child("likes")
                                .child(FirebaseAuth.getInstance().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.likered, 0, 0, 0);
                                                }else{
                                                    holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            FirebaseDatabase.getInstance().getReference().child("posts")
                                                                    .child(model.getPostId())
                                                                    .child("likes")
                                                                    .child(FirebaseAuth.getInstance().getUid()).setValue(true)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("posts")
                                                                                    .child(model.getPostId())
                                                                                    .child("postlike")
                                                                                    .setValue(model.getPostLike()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.likered, 0, 0, 0);

                                                                                            Notification notification=new Notification();
                                                                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                                            notification.setNotificationAt(new Date().getTime());
                                                                                            notification.setPostId(model.getPostId());
                                                                                            notification.setPostedBy(model.getpostedBy());
                                                                                            notification.setType("Like");

                                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                                    .child("notification")
                                                                                                    .child(model.getpostedBy())
                                                                                                    .push()
                                                                                                    .setValue(notification);
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("postId",model.getPostId());
                intent.putExtra("postedBy",model.getpostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        DashbordRvSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=DashbordRvSampleBinding.bind(itemView);
        }
    }
}
