package edu.handong.isel.allgitclone.act;

import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.handong.isel.allgitclone.control.GithubService;
import edu.handong.isel.allgitclone.control.RetroBasic;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CommitActivity {
	
	private Retrofit retrofit;
	private boolean blocked = false;		//indicate which the page is over 10 or not.
	private String lastDate;			//standard
	

	public CommitActivity(String token) {
		retrofit = new RetroBasic().createObject(token);
	}
	
	
	public void start (HashMap<String, String> commitOption, HashSet<String> finalResult) {
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonObject> request = service.getUserCommits(commitOption);
		
		
		try {
			Response<JsonObject> response = request.execute();
			
			if (response.message().equals("Forbidden")) {
				System.out.println("Commit : Waiting for request ...");
				blocked = true;
				return;
			}
			
			else
				blocked = false;
			
			
			JsonArray jsArr = new Gson().fromJson(response.body().get("items"), JsonArray.class);
			
			if (jsArr.size() == 0)
				return;
			
			
			for (int i = 0; i < jsArr.size(); i++) {
				JsonObject item = new Gson().fromJson(jsArr.get(i), JsonObject.class);
				JsonObject commits = new Gson().fromJson(item.get("commit"), JsonObject.class);
				JsonObject author = new Gson().fromJson(commits.get("author"), JsonObject.class);
				JsonObject repo = new Gson().fromJson(item.get("repository"), JsonObject.class);
				
				finalResult.add(repo.get("html_url").getAsString());
				
				if (i == jsArr.size() - 1)
					lastDate = author.get("date").getAsString();

			}
			
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	

	public boolean isBlocked() {
		return blocked;
	}

	public String lastDate() {
		return lastDate;
	}
	
}
