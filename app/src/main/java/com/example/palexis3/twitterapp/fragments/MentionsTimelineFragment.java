package com.example.palexis3.twitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.palexis3.twitterapp.TwitterApp;
import com.example.palexis3.twitterapp.clients.TwitterClient;
import com.example.palexis3.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MentionsTimelineFragment extends TweetsListFragment{
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        populateTimeline();
    }

    // Send PI request to get timeline json
    // fill the listview by creating the tweet objects from the json
    private void populateTimeline(){
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG SUCCESS", response.toString());

                //populate
                addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG FAILURE", errorResponse.toString());
            }
        });
    }

    @Override
    protected void getOlderTweets(long lastTweetId) {
        //Get the client
        client = TwitterApp.getRestClient(); //singleton client
        client.getOlderMentionsTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG SUCCESS", response.toString());

                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                if (newTweets.size() > 0) {
                    // using max_id is inclusive, we need to find this tweet and remove it from
                    // the array because we already have it in our list of tweets.
                    // It should be the first item in the list.
                    newTweets.remove(0);
                    aTweets.addAll(newTweets);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG FAILURE", errorResponse.toString());
            }
        }, lastTweetId);
    }
}
