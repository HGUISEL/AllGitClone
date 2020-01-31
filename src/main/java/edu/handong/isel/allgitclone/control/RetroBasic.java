package edu.handong.isel.allgitclone.control;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroBasic {
	
	private Retrofit retrofit;
	private static String BASE_URL = "https://api.github.com/";
	
	public void createObject() {
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				
				/*
				 * token : personal access token
				 */
				String token = "2c9a54fcfed48f5cbdd225c3070a75cfda7a55cc";
				Request newRequest = chain.request().newBuilder()
						.addHeader("Authorization", "token " + token)
						.build();
				
				return chain.proceed(newRequest);
			}
		}).build();
		
		retrofit = new Retrofit.Builder()
				.client(client)
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}
	
	public Retrofit getObject() {
		return retrofit;
	}
	
}