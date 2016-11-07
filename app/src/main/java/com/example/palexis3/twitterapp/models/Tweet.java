package com.example.palexis3.twitterapp.models;

import com.example.palexis3.twitterapp.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// Parse the JSON + store date, encapsulate state logic or display logic
public class Tweet {
    private String body;
    private long uid; // unique id for tweet
    private User user;
    private String createdAt;
    private boolean isFavorited;
    private long favoritesCount;
    private long retweetCount;

    public long getRetweetCount() {
        return retweetCount;
    }



    public long getFavoritesCount() {
        return favoritesCount;
    }



    public boolean isFavorited() {
        return isFavorited;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRelativeCreatedAt() {return Utilities.getRelativeTimeAgo(createdAt);}

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }


    //Deserialize json
    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.isFavorited = jsonObject.getBoolean("favorited");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.favoritesCount = jsonObject.getLong("favorite_count");
            tweet.retweetCount = jsonObject.getLong("retweet_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null) {
                 tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
