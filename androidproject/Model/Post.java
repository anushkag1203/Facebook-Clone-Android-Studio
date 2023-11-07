package com.example.androidproject.Model;

public class Post {

    private String postId;
    private String posiImage;
    private String postedBy;
    private String postDescription;
    private long postedAt;

    private int postlike;
    private int commentCount;
    public Post() {

    }

    public Post(String postId, String posiImage, String postedBy, String postDescription, long postedAt,int postlike) {
        this.postId = postId;
        this.posiImage = posiImage;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postedAt = postedAt;
        this.postlike = postlike;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPosiImage() {
        return posiImage;
    }

    public void setPosiImage(String posiImage) {
        this.posiImage = posiImage;
    }

    public String getpostedBy() {
        return postedBy;
    }

    public void setpostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public int getPostLike() {
        return postlike;
    }

    public void setPostLike(int postlike) {
        this.postlike = postlike;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
