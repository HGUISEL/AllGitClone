package edu.handong.isel.allgitclone.control;

import java.util.Map;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface GithubService {
	
	@Headers("Accept: application/vnd.github.cloak-preview")
	@GET("repos/{owner_repo}/commits")
	Call<JsonObject> getUserCommits(@Path("owner_repo") String owner_repo);
	
	@Headers("Accept: application/vnd.github.mercy-preview+json")
	@GET("search/repositories")
	Call<JsonObject> getJavaRepositories(@QueryMap Map<String, String> lang);
}