package com.duaduatib.eduforum.model;

import java.util.List;

public class Data {
    private Post post;
    private List<Post> posts;
    private String access_token;

    public List<Post> getPosts() {
        return posts;
    }

    public String getAccess_Token() {
        return access_token;
    }

    public Post getPost() {
        return post;
    }
}
