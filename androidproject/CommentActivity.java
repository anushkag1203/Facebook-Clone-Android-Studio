package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androidproject.Adapter.CommentAdapter;
import com.example.androidproject.Model.Notification;
import com.example.androidproject.Model.Post;
import com.example.androidproject.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    Intent intent;
    String postId;
    String postedBy;

    FirebaseDatabase database;
    FirebaseAuth auth;

    ArrayList<Comment> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        binding=ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent=getIntent();

        setSupportActionBar(binding.toolbar);
        CommentActivity.this.setTitle("Comments");

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        postId=intent.getStringExtra("postId");
        postedBy=intent.getStringExtra("postedBy");

        database.getReference().child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Post post=snapshot.getValue(Post.class);
                        Picasso.get().load(post.getPosiImage())
                                .placeholder(R.drawable.profile)
                                .into(binding.commentBackgroundImage);

                        binding.description.setText(post.getPostDescription());
                        binding.commentLike.setText(post.getPostLike()+"");
                        binding.commentComment.setText(post.getCommentCount()+"");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        database.getReference().child("users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile_image())
                                .placeholder(R.drawable.profile)
                                .into(binding.commentImage);

                        binding.commentName.setText(user.getFirstName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.commentPost.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                Comment comment=new Comment();
                comment.setCommentBody(binding.commentET.getText().toString());
                comment.setCommentAt(new Date().getTime());
                comment.setCommentBy(FirebaseAuth.getInstance().getUid());


                database.getReference().child("posts")
                        .child(postId)
                        .child("comments").
                         push().
                        setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("posts")
                                        .child(postId)
                                        .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int commentCount=0;

                                                if(snapshot.exists()){
                                                    commentCount=snapshot.getValue(Integer.class);
                                                }
                                                database.getReference().child("posts")
                                                        .child(postId)
                                                        .child("commentCount")
                                                        .setValue(commentCount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                binding.commentET.setText("   ");
                                                                Toast.makeText(CommentActivity.this, "Commented", Toast.LENGTH_SHORT).show();
                                                                Notification notification=new Notification();
                                                                notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                notification.setNotificationAt(new Date().getTime());
                                                                notification.setPostId(postId);
                                                                notification.setPostedBy(postedBy);
                                                                notification.setType("Comment");

                                                                FirebaseDatabase.getInstance().getReference()
                                                                        .child("notification")
                                                                        .child(postedBy)
                                                                        .push()
                                                                        .setValue(notification);


                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });
            }
        });


        CommentAdapter adapter=new CommentAdapter(this,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.commentRV.setLayoutManager(linearLayoutManager);
        binding.commentRV.setAdapter(adapter);

        database.getReference().child("posts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Comment comment=dataSnapshot.getValue(Comment.class);
                            list.add(comment);

                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}