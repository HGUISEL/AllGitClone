# AllGitClone
>A tool which helps to get various url of github repositories what user wants.


## Retrofit + OkHttp
>How to communicate to web Server as Client?
```java
@GET("search/repositories")
Call<JsonObject> getJavaRepositories(@QueryMap Map<String, String> lang);
```

  - query to search (ex: api.github.com/search/repositories?q=language:java)


## Declaration to communicating object
```java
Retrofit retro = new Retrofit.Builder()
                              .baseUrl(BASE_URL)
                              .addConverterFactory(GsonConverterFactory.create())
                              .build();
```

## Which contents we have to get?
>Search appropriate repos according to given options

>Base option : created_at (created date of repo)


## How to divide many results using options?
```java
String lang = "java";
String created = "2015-1-1";
int pages = 1;

//TODO (need loop funct)
change_date(created);

pages++;

String query_options = "language:" + lang +
                       " created:" + created;
                       
                       
options.replace("q", query_options);

//Option Query Map
options.put("q", query_options);
options.put("page", String.valueOf(pages));
options.put("sort", "forks");
options.put("per_page", String.valueOf(100));
```

>Need to implement the function which changes date.


## Multi-options
> Need to gather informations to add another options like commit, repo size, etc.


## Result
> Repo URL that meets given criteria
