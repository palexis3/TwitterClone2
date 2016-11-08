package com.example.palexis3.twitterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.palexis3.twitterapp.R;
import com.example.palexis3.twitterapp.TwitterApp;
import com.example.palexis3.twitterapp.TwitterClient;
import com.example.palexis3.twitterapp.activities.TimelineActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TweetComposerActivity extends AppCompatActivity {

    private TextView tvMessage;
    private TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_composer);
        client = TwitterApp.getRestClient();
    }

    public void sendTweet(View view) {
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        String tweet = tvMessage.getText().toString();
        if (!tweet.isEmpty()) {
            client.sendTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG SUCCESS", response.toString());
                    Intent i = new Intent(getBaseContext(), TimelineActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG FAILURE", errorResponse.toString());
                }
            }, tweet);
        }
    }
}
