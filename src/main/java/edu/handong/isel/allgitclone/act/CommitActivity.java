package edu.handong.isel.allgitclone.act;

import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import edu.handong.isel.allgitclone.control.GithubService;
import edu.handong.isel.allgitclone.control.RetroBasic;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CommitActivity {
	
	private Retrofit retrofit;
	private boolean blocked = false;		//indicate which the page is over 10 or not.


	public CommitActivity(String token) {
		this.retrofit = new RetroBasic().createObject(token);
	}
	
	
	public void start (int range, boolean excessable, String owner_repo, HashSet<String> finalResult) {
		String[] arr = owner_repo.split("\\/");
		int yearlyCommit = 0;
		
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonArray> request = service.getUserCommits(arr[0], arr[1]);
		
		
		try {
			System.out.println(request.toString());
			Response<JsonArray> response = request.execute();
			
			if (response.message().equals("Forbidden")) {
				System.out.println("Commit : Waiting for request ...");
				blocked = true;
				return;
			}
			
			else
				blocked = false;
			
			
			for (int i = 0; i < response.body().size(); i++) {
				JsonObject total = new Gson().fromJson(response.body().get(i), JsonObject.class);
				yearlyCommit += total.get("total").getAsInt();
			}
			
			if (excessable) {
				if (yearlyCommit >= range)
					finalResult.add(owner_repo + "#" + yearlyCommit);
			} else {
				if (yearlyCommit <= range)
					finalResult.add(owner_repo + "#" + yearlyCommit);
			}
		}
		
		catch(JsonSyntaxException jse) {
			
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	

	public boolean isBlocked() {
		return blocked;
	}
}
