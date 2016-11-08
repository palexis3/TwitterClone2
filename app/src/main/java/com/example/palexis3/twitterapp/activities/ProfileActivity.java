package com.example.palexis3.twitterapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.palexis3.twitterapp.R;
import com.example.palexis3.twitterapp.TwitterClient;
import com.example.palexis3.twitterapp.fragments.UserTimelineFragment;
import com.example.palexis3.twitterapp.models.User;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ProfileActivity extends AppCompatActivity {
    private TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = (User) getIntent().getSerializableExtra("user");

        getSupportActionBar().setTitle(user.getScreenName());

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