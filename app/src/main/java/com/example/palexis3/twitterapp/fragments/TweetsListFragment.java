package com.example.palexis3.twitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.palexis3.twitterapp.EndlessScrollListener;
import com.example.palexis3.twitterapp.R;
import com.example.palexis3.twitterapp.TweetsArrayAdapter;
import com.example.palexis3.twitterapp.TwitterClient;
import com.example.palexis3.twitterapp.models.Tweet;

import java.util.ArrayList;
import java.util.List;


public abstract class TweetsListFragment extends Fragment {
    // Inflation logic
    private TwitterClient client;
    protected TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);

        // Connect adaptor to list view
        lvTweets.setAdapter(aTweets);

        //Handlers
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Get the last tweets id to cursor the api
                long lastTweetId = tweets.get(tweets.size() -1).getUid();
                getOlderTweets(lastTweetId);
                return true;
            }
        });
        return v;
    }

    // Creation lifecycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create array list
        tweets = new ArrayList<>();
        // Construct adaptor
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

        // TODO: enforce populateTimeline
    }

    protected abstract void getOlderTweets(long lastTweetId);
    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }
}
