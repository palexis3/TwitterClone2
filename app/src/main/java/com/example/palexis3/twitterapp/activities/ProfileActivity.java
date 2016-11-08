package com.example.palexis3.twitterapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.palexis3.twitterapp.R;
import com.example.palexis3.twitterapp.TwitterApp;
import com.example.palexis3.twitterapp.TwitterClient;
import  com.example.palexis3.twitterapp.fragments.UserTimelineFragment;
import com.example.palexis3.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ProfileActivity extends AppCompatActivity {
    private TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        boolean isMainProfile = getIntent().getBooleanExtra("isMainProfile", false);

        if(isMainProfile) {
            client = TwitterApp.getRestClient();

            //Get account info
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJson(response);
                }
            });

            getSupportActionBar().setTitle(user.getScreenName());

        } else {
            user =  (User) getIntent().getSerializableExtra("user");
        }

        // Get the screen name
        String screenName = getIntent().getStringExtra("screen_name");

        populateProfileHeader(user);

        // Hasn't been created before. Do work.
        if (savedInstanceState == null) {
            //Create user timeline fragment
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            // Display user fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit(); // changes fragments
        }
    }

    private void populateProfileHeader(User user) {
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileActivityImage);
        TextView tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        TextView tvTagline = (TextView) findViewById(R.id.tvProfileTagline);
        TextView tvFollowersCount = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowingCount = (TextView) findViewById(R.id.tvFollowing);

        ivProfileImage.setImageResource(android.R.color.transparent); // clear out old image
        Picasso.with(this).load(user.getProfileImageUrl()).transform(new RoundedCornersTransformation(1, 1)).into(ivProfileImage);
        tvProfileName.setText(user.getName());
        tvTagline.setText(user.getTagLine());
        tvFollowersCount.setText(user.getFollowersCount() + " Followers");
        tvFollowingCount.setText(user.getFollowingCount() + " Following");
    }
}