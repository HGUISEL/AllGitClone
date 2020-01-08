package edu.handong.isel.allgitclone.control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity {
	
	private static Retrofit retrofit = null;
	private boolean check_blank = false;
	private boolean check_over_limits = false;
	private int last_forks = 0;
	
	public MainActivity() {
		RetroBasic new_object = new RetroBasic();
		new_object.createObject();
		retrofit = new_object.getObject();
	}
	
	public void runRepoService(String userName) {
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonArray> request = service.getUserRepositories(userName);
		
		
		//Asynchronous method (Synchronous : request.execute())
		request.enqueue(new Callback<JsonArray>() {
			@Override
			public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
				
				System.out.println("Total count : " + response.body().size());
				
				for (int i = 0; i < response.body().size(); i++) {
					
					JsonObject json_resp = new Gson().fromJson(response.body().get(i), JsonObject.class);
					//JsonObject json_resp_owner = new Gson().fromJson(json_resp.get("owner"), JsonObject.class);
					
					System.out.println(json_resp.get("id") + "\n" +
									json_resp.get("node_id") + "\n" +
									json_resp.get("html_url") + "\n" +
									json_resp.get("created_at") + "\n" +
									json_resp.get("git_url") + "\n" +
									json_resp.get("default_branch") + "\n\n");
				}
			}
			
			@Override
			public void onFailure(Call<JsonArray> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}
	
	public void runCommitService(String userName, String repoName) {
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonArray> crequest = service.getUserCommits(userName, repoName);
		crequest.enqueue(new Callback<JsonArray>() {
			@Override
			public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
				
				for (int i = 0; i < response.body().size(); i++) {
					
					JsonObject json_com = new Gson().fromJson(response.body().get(i), JsonObject.class);
					JsonObject json_com_detail = new Gson().fromJson(json_com.get("commit"), JsonObject.class);
					
					System.out.println(json_com.get("sha") + "\n" +
									json_com.get("node_id") + "\n" +
									json_com_detail.get("message") + "\n\n");
				}
			}
			
			@Override
			public void onFailure(Call<JsonArray> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}
	
	
	//Synchronous method
	public void runSyncService(BufferedWriter bw, PrintWriter pw, HashMap<String, String> options) throws IOException {
		
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonObject> lrequest = service.getJavaRepositories(options);

		
		try {
			Response<JsonObject> response = lrequest.execute();
			
			if (response.message().equals("Forbidden")) {
				System.out.println("The contents are over 1000 counts.\n");
				check_over_limits = true;
				return;
			}
			
			JsonArray json_com = new Gson().fromJson(response.body().get("items"), JsonArray.class);
			
			
			if (json_com.size() == 0) {
				System.out.println("There is no content ever.");
				check_blank = true;
				return;
			}
			
			for (int i = 0; i < json_com.size(); i++) {
				JsonObject item = new Gson().fromJson(json_com.get(i), JsonObject.class);
				String line = item.get("html_url") + "\n" +
							item.get("description") + "\n" +
							item.get("forks") + "\n" +
							item.get("created_at") + "\n\n";
				
				System.out.println(line);
				
				if (i == json_com.size() - 1)
					last_forks = item.get("forks").getAsInt() - 1;
			}
			
		}
		
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	//Asynchronous method
	public void runAsyncService(BufferedWriter bw, PrintWriter pw, HashMap<String, String> options) throws IOException {
		
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonObject> lrequest = service.getJavaRepositories(options);
		
		lrequest.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				
				//If the contents are over 1000, TODO some solutions to crawl other various contents.
				if (response.message().equals("Forbidden")) {
					System.out.println("The contents are over 1000 counts.\n");
					check_over_limits = true;
					return;
				}
				
				JsonArray json_com = new Gson().fromJson(response.body().get("items"), JsonArray.class);

				if (json_com.size() == 0) {
					System.out.println("There is no content ever.");
					check_blank = true;
					return;
				}
				

				
				
				//if we want to set fork counts options, we can make if statement using item.get("forks") method
				for (int i = 0; i < json_com.size(); i++) {
					JsonObject item = new Gson().fromJson(json_com.get(i), JsonObject.class);
					String line = item.get("html_url") + "\n" +
									item.get("description") + "\n" +
									item.get("forks") + "\n" +
									item.get("created_at") + "\n\n";
					
					System.out.println(line);
					
					if (i == json_com.size() - 1)
						last_forks = item.get("forks").getAsInt() - 1;
					
					pw.write(line);
					pw.flush();
				}
				
			}
		
			@Override
			public void onFailure(Call<JsonObject> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}
	
	public Retrofit getRetrofit() {
		return retrofit;
	}

	public boolean isCheck_blank() {
		return check_blank;
	}

	public boolean isCheck_over_limits() {
		return check_over_limits;
	}

	public int getLast_forks() {
		return last_forks;
	}
	
	public static void setRetrofit(Retrofit retrofit) {
		MainActivity.retrofit = retrofit;
	}

	public void setCheck_blank(boolean check_blank) {
		this.check_blank = check_blank;
	}

	public void setCheck_over_limits(boolean check_over_limits) {
		this.check_over_limits = check_over_limits;
	}

	public void setLast_forks(int last_forks) {
		this.last_forks = last_forks;
	}
	
}
