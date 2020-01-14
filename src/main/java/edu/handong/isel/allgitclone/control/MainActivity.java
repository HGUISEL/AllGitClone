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
	private boolean check_over_limits = false;		//indicate which the page is over 10 or not.
	private String last_pushed = null;			//standard

	public MainActivity() {
		RetroBasic new_object = new RetroBasic();
		new_object.createObject();
		retrofit = new_object.getObject();
	}
	
	public void runCommitService(HashMap<String, String> options) {
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonObject> crequest = service.getUserCommits(options);
		
		try {
			Response<JsonObject> response = crequest.execute();
			
			if (response.message().equals("Forbidden")) {
				System.out.println("Request is being processed. Please wait.\n");
				check_over_limits = true;
				return;
			}
			
			else
				check_over_limits = false;
			
			
			JsonArray json_com = new Gson().fromJson(response.body().get("items"), JsonArray.class);
			
			if (json_com.size() == 0) {
				System.out.println("There is no content ever.");
				check_blank = true;
				return;
			}
			
			for (int i = 0; i < json_com.size(); i++) {
				JsonObject item = new Gson().fromJson(json_com.get(i), JsonObject.class);
				JsonObject commits = new Gson().fromJson(item.get("commit"), JsonObject.class);
				JsonObject reposit = new Gson().fromJson(item.get("repository"), JsonObject.class);
				
				String line = item.get("sha") + "\n" +
							commits.get("message")+ "\n" +
							reposit.get("full_name") + "\n\n";
						
				
				System.out.println(line);
				
				if (i == json_com.size() - 1)
					last_pushed = item.get("pushed_at").getAsString();
			}
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	//Synchronous method
	public void runSyncService(BufferedWriter bw, PrintWriter pw, HashMap<String, String> options) throws IOException {
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonObject> lrequest = service.getJavaRepositories(options);
	
		try {
			Response<JsonObject> response = lrequest.execute();
			
			if (response.message().equals("Forbidden")) {
				System.out.println("Request is being processed. Please wait.\n");
				check_over_limits = true;
				return;
			}
			
			else
				check_over_limits = false;
			
			
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
							item.get("created_at") + "\n" +
							item.get("pushed_at") + "\n\n";
				
				String stored_line =
							item.get("description") + "," +
							item.get("forks") + "," +
							item.get("created_at") + "," +
							item.get("pushed_at") + "," +
							item.get("html_url") + "\n\n";
				
				System.out.println(line);
				pw.write(stored_line);
				pw.flush();
				
				if (i == json_com.size() - 1)
					last_pushed = item.get("pushed_at").getAsString();
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
									item.get("created_at") + "\n" +
									item.get("pushed_at") + "\n\n";
					
					System.out.println(line);
					
					if (i == json_com.size() - 1)
						last_pushed = item.get("pushed_at").getAsString();
					
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
	
	public String getLast_pushed() {
		return last_pushed;
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
	
	public void setLast_pushed(String last_pushed) {
		this.last_pushed = last_pushed;
	}
	
}
