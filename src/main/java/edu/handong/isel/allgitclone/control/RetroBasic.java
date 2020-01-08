package edu.handong.isel.allgitclone.control;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroBasic {
	
	private Retrofit retrofit;
	private static String BASE_URL = "https://api.github.com/";
	
	public void createObject() {
		Retrofit retro = new Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		
		retrofit = retro;
	}
	
	public Retrofit getObject() {
		return retrofit;
	}
	
}
