package edu.handong.isel.allgitclone.control;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GithubService {
	//https://api.github.com/user/userName/repos
	@GET("users/{user}/repos")
	//@Path : replace variable of API endpoint
	Call<JsonArray> getUserRepositories(@Path("user") String userName);
	
	
	//https://api.github.com/repos/userName/repoName/commits
	@GET("repos/{user}/{repo}/commits")
	Call<JsonArray> getUserCommits(@Path("user") String userName, @Path("repo") String repoName);
	
	@GET("search/repositories")
	Call<JsonObject> getJavaRepositories(@QueryMap Map<String, String> lang);
}
