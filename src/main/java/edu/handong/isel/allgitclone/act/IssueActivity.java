package edu.handong.isel.allgitclone.act;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.handong.isel.allgitclone.control.GithubService;
import edu.handong.isel.allgitclone.control.RetroBasic;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class IssueActivity {

	private static Retrofit retrofit = null;
	private boolean check_blank = false;
	private boolean check_over_limits = false;		//indicate which the page is over 10 or not.
	private String last_date = null;			//standard

	
	public IssueActivity() {
		RetroBasic new_object = new RetroBasic();
		new_object.createObject();
		retrofit = new_object.getObject();
	}
	
	
	public void start(BufferedWriter bw, PrintWriter pw, HashMap<String, String> issueOption) {
		GithubService service = retrofit.create(GithubService.class);
		Call<JsonObject> request = service.getJavaRepositories(issueOption);
	
		try {
			Response<JsonObject> response = request.execute();
			
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
				String line = "";
				
				JsonObject item = new Gson().fromJson(json_com.get(i), JsonObject.class);
				JsonArray labels = new Gson().fromJson(item.get("labels"), JsonArray.class);
				
				
				
				for (int j = 0; j < labels.size(); j++) {
					JsonObject label = new Gson().fromJson(labels.get(j), JsonObject.class);
					line = label.get("name") + "\n" +
							label.get("description") + "\n";
						
				}
				
				line += item.get("html_url") + "\n" +
						item.get("pushed_at") + "\n\n";
				
				
				System.out.println(line);
				
				
				if (i == json_com.size() - 1)
					last_date = item.get("pushed_at").getAsString();
			}
			
		}
		
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public boolean isCheck_blank() {
		return check_blank;
	}

	public boolean isCheck_over_limits() {
		return check_over_limits;
	}

	public String getLast_date() {
		return last_date;
	}
	
	
}
