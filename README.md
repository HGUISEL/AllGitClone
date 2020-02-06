# AllGitClone
>A tool which helps to get various url of github repositories what user wants.<br /><br />


## Retrofit + OkHttp
>How to communicate to web Server as Client?
```java
@GET("search/repositories")
Call<JsonObject> getJavaRepositories(@QueryMap Map<String, String> lang);
```

  - query to search (ex: api.github.com/search/repositories?q=language:java)<br /><br />


## Declaration to communicating object
```java
Retrofit retro = new Retrofit.Builder()
                              .baseUrl(BASE_URL)
                              .addConverterFactory(GsonConverterFactory.create())
                              .build();
```  


## Overview
![relation chart](https://user-images.githubusercontent.com/49176685/73900207-5edb8c00-48d2-11ea-876d-3e4832ef579e.png)



## Available Options (2020-01-31 updated)
> Repository option

- -l  :  language (java, c, c++, apache, etc.)  <br />

- -f  :  fork count (inequality sign, .. {range sign})  <br />

- -d  :  date (last pushed date)  <br /><br />



> Commit option

- -ad  :  author-date (date that author committed most recently)  <br /><br />



> Others

- -auth  :  authentication token (user's personal access token)  <br /><br />



I have a plan to add more possible options.  <br /><br />



## Example
```linux
AllGitClone -l java -f 100..2000 -ad 2019-01-01..2020-01-01 -auth 'personal_token'  
```

This means that we can get a list of repositories, written in java, have fork counts greater than 100 and less than 2000,  <br />

and whose last committed date is 2019-01-01 to 2020-01-01, using personal authentication token.

