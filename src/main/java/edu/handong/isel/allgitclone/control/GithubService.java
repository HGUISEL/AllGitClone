package edu.handong.isel.allgitclone.control;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface GithubService {
	
	@Headers("Accept: application/vnd.github.v3+json")
	@GET("repos/{owner}/{repo}/stats/commit_activity")
	Call<JsonArray> getUserCommits(@Path("owner") String owner, @Path("repo") String repo);
	
	@Headers("Accept: application/vnd.github.mercy-preview+json")
	@GET("search/repositories")
	Call<JsonObject> getJavaRepositories(@QueryMap Map<String, String> lang);
}