package com.example.palexis3.twitterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.palexis3.twitterapp.R;
import com.example.palexis3.twitterapp.TwitterApp;
import com.example.palexis3.twitterapp.TwitterClient;
import com.example.palexis3.twitterapp.fragments.HomeTimelineFragment;
import com.example.palexis3.twitterapp.fragments.MentionsTimelineFragment;
import com.example.palexis3.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private User user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_timeline, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_timeline);

        //Get viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set the viewpager adapter
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //find the pager sliding tabs
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //attach the pager tabs to viewpager
        tabStrip.setViewPager(vpPager);
    }

    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(this, TweetComposerActivity.class);
        startActivity(i);
    }

    public void onProfileView(MenuItem item) {
        // launch profile view
        client = TwitterApp.getRestClient();

        //Get account info
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                user = User.fromJson(response);
                i.putExtra("user", user);
                i.putExtra("screen_name", user.getScreenName());
                startActivity(i);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Couldn't find user", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Return order of fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
