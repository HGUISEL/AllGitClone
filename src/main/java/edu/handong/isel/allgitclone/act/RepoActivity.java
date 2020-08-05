package edu.handong.isel.allgitclone.act;

import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.handong.isel.allgitclone.control.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RepoActivity {

	private Retrofit retrofit;
	private boolean blank = false;
	private boolean blocked = false;		//indicate which the page is over 10 or not.
	private String lastDate;			//standard
	private HashSet<String> repoResult;
	
	
	public RepoActivity(String token) {
		repoResult = new HashSet<>();
		retrofit = new RetroBasic().createObject(token);
	}
	
	
	public void start (HashMap<String, String> repoOption) {
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonObject> request = service.getJavaRepositories(repoOption);
	
		try {
			Response<JsonObject> response = request.execute();
			
			if (response.message().equals("Forbidden")) {
				System.out.println("Repo : Waiting for request ...");
				blocked = true;
				return;
			}
			
			else
				blocked = false;
			
			
			JsonArray jsArr = new Gson().fromJson(response.body().get("items"), JsonArray.class);
			
			if (jsArr.size() == 0) {
				System.out.println("There is no content ever.");
				blank = true;
				return;
			}
			
			
			for (int i = 0; i < jsArr.size(); i++) {
				JsonObject item = new Gson().fromJson(jsArr.get(i), JsonObject.class);
				repoResult.add(item.get("full_name").getAsString());	
				
				if (i == jsArr.size() - 1)
					lastDate = item.get("pushed_at").getAsString();
			}
			
		}
		
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	

	public HashSet<String> getRepoResult() {
		return repoResult;
	}


	public boolean isBlank() {
		return blank;
	}


	public boolean isBlocked() {
		return blocked;
	}


	public String lastDate() {
		return lastDate;
	}
	
}
