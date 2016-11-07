package com.example.palexis3.twitterapp;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/"; //base API URL
	public static final String REST_CONSUMER_KEY = "NU9mPGg9pElEW9KidVU5tssFe";
	public static final String REST_CONSUMER_SECRET = "JDkcxpDgYEJfFwnyFFrcug1tcZXb2SNPd7LepFk9YyzSodlKWF";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";


	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// METHOD == ENDPOINT

	// HOMETIMELINE - Gets us the home timeline

	//GET statuses/home_timeline.json
	//		count=25
	//since_id=1
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		executeTimelineRequest(-1, -1, null, handler, "home_timeline.json");
	}

	public void getOlderHomeTimeline(AsyncHttpResponseHandler handler, long maxId) {
		executeTimelineRequest(-1, maxId, null, handler, "home_timeline.json");
	}

	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
		executeTimelineRequest(-1, -1, null, handler, "mentions_timeline.json");
	}

	public void getOlderMentionsTimeline(AsyncHttpResponseHandler handler, long maxId) {
		executeTimelineRequest(-1, maxId, null, handler, "mentions_timeline.json");
	}

	public void getUserTimeline(AsyncHttpResponseHandler handler, String screenName) {
		executeTimelineRequest(-1, -1, screenName, handler, "user_timeline.json");
	}

	public void getOlderUserTimeline(AsyncHttpResponseHandler handler, String screenName, long maxId) {
		executeTimelineRequest(-1, maxId, screenName, handler, "user_timeline.json");
	}

	public void getUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		// Execute request
		getClient().get(apiUrl, null, handler);
	}

	public void likeTweet(AsyncHttpResponseHandler handler, long tweetId) {
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		// Execute request
		getClient().post(apiUrl, params, handler);
	}

	public void unlikeTweet(AsyncHttpResponseHandler handler, long tweetId) {
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		// Execute request
		getClient().post(apiUrl, params, handler);
	}

	private void executeTimelineRequest(long sinceId, long maxId, String screenName, AsyncHttpResponseHandler handler, String resource) {
		String apiUrl = getApiUrl(String.format("statuses/%s", resource));
		RequestParams params = new RequestParams();
		params.put("count", 25);

		if (sinceId > 0) {
			params.put("since_id", sinceId);
		}

		if (maxId > 0) {
			params.put("max_id", maxId);
		}

		if (screenName != null && !screenName.isEmpty()) {
			params.put("screen_name", screenName);
		}
		// Execute request
		getClient().get(apiUrl, params, handler);
	}


	//COMPOSE TWEET

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
	public void sendTweet(AsyncHttpResponseHandler handler, String text) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", text);

		// Execute request
		getClient().post(apiUrl, params, handler);
	}
}