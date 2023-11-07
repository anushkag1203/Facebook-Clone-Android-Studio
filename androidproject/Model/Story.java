package com.example.androidproject.Model;

import java.util.ArrayList;

//the model is required as we have multiple widgets and we don't have a single datatype that handles it
//so we need model to handle all the data types seprately
public class Story {

    private String storyBy;
    private long storyAt;

    ArrayList<UserStories> stories;

    public Story(String storyBy, long storyAt, ArrayList<UserStories> stories) {
        this.storyBy = storyBy;
        this.storyAt = storyAt;
        this.stories = stories;
    }

    public Story() {
    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UserStories> getStories() {
        return stories;
    }

    public void setStories(ArrayList<UserStories> stories) {
        this.stories = stories;
    }
}
