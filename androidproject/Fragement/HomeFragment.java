package com.example.androidproject.Fragement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidproject.Adapter.PostAdapter;
import com.example.androidproject.Adapter.StoryAdapter;
import com.example.androidproject.Model.Post;
import com.example.androidproject.Model.Story;
import com.example.androidproject.Model.UserStories;
import com.example.androidproject.R;
import com.example.androidproject.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    RecyclerView storyRv,dashboardRv;
    ArrayList<Story> storyList;
    ArrayList<Post> postlist;

    ImageView addStory;
    FirebaseDatabase database;
    FirebaseAuth auth;

    ImageView addStoryImage;
    RoundedImageView story;
    FirebaseStorage storage;

    ActivityResultLauncher<String > gallery;

    ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();


        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Story Uploading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        storyRv = view.findViewById(R.id.storyRV);

        storyList = new ArrayList<>();


        StoryAdapter adapter = new StoryAdapter(storyList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);

        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    storyList.clear();
                    for (DataSnapshot storySnapShot : snapshot.getChildren()) {
                        Story story = new Story();
                        story.setStoryBy(storySnapShot.getKey());
                        story.setStoryAt(storySnapShot.child("postedBy").getValue(Long.class));

                        ArrayList<UserStories> stories = new ArrayList<>();
                        for (DataSnapshot snapshot1 : storySnapShot.child("userStories").getChildren()) {
                            UserStories userStories = snapshot1.getValue(UserStories.class);
                            stories.add(userStories);

                        }
                        story.setStories(stories);
                        storyList.add(story);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // dashboard recycle view
        dashboardRv = view.findViewById(R.id.dashboardRV);
        postlist = new ArrayList<>();


        PostAdapter postAdapter = new PostAdapter(postlist, getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        dashboardRv.setLayoutManager(linearLayoutManager1);
        dashboardRv.setNestedScrollingEnabled(false);
        dashboardRv.setAdapter(postAdapter);

        database.getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postlist.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            post.setPostId(dataSnapshot.getKey());
                            postlist.add(post);

                        }
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        addStoryImage = view.findViewById(R.id.imageView3);
        story = view.findViewById(R.id.storyImage);
        addStoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallery.launch("image/*");
            }
        });

        gallery = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                story.setImageURI(o);
                progressDialog.show();
                final StorageReference reference = storage.getReference()
                        .child("stories")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime() + "");
                reference.putFile(o).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Story story = new Story();
                                story.setStoryAt(new Date().getTime());
                                database.getReference()
                                        .child("stories")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .child("postedBy")
                                        .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                UserStories stories = new UserStories(uri.toString(), story.getStoryAt());
                                                database.getReference()
                                                        .child("stories")
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .child("userStories")
                                                        .push()
                                                        .setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                progressDialog.dismiss();
                                                            }
                                                        });

                                            }
                                        });
                            }
                        });
                    }
                });

            }
        });


        return view;

}}