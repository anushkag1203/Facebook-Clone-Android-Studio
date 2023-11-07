package com.example.androidproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.Model.Story;
import com.example.androidproject.Model.UserStories;
import com.example.androidproject.R;
import com.example.androidproject.User;
import com.example.androidproject.databinding.StoryRvDesignBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder> {

    ArrayList<Story>list;
    Context context;

    public StoryAdapter(ArrayList<Story> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//design inflate
        View view= LayoutInflater.from(context).inflate(R.layout.story_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {//set data on views
        Story story=list.get(position);

        if(story.getStories().size()>0) {
            UserStories lastStory = story.getStories().get(story.getStories().size() - 1);
            Picasso.get().load(lastStory.getImage())
                    .into(holder.binding.storyImage);

            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Picasso.get().load(user.getProfile_image())
                                    .placeholder(R.drawable.profile)
                                    .into(holder.binding.friendImage);

                            holder.binding.name.setText(user.getFirstName());

                            holder.binding.storyImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    ArrayList<MyStory> myStories = new ArrayList<>();

                                    for (UserStories stories : story.getStories()) {
                                        myStories.add(new MyStory(
                                                stories.getImage()

                                        ));
                                    }

                                    new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                            .setStoriesList(myStories)
                                            .setStoryDuration(5000)
                                            .setTitleText(user.getFirstName())
                                            .setSubtitleText("")
                                            .setTitleLogoUrl(user.getProfile_image())
                                            .setStoryClickListeners(new StoryClickListeners() {
                                                @Override
                                                public void onDescriptionClickListener(int position) {
                                                    //your action
                                                }

                                                @Override
                                                public void onTitleIconClickListener(int position) {
                                                    //your action
                                                }
                                            }) // Optional Listeners
                                            .build() // Must be called before calling show method
                                            .show();

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        StoryRvDesignBinding binding;
        public viewHolder(@NonNull View itemView) {//viewholder for holding all the views on the page
            super(itemView);

            binding=StoryRvDesignBinding.bind(itemView);
        }
    }
}
