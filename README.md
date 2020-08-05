# AllGitClone
>A system that searches and lists the Github repositories that users need based on criteria.<br />
>The overall design of the program was referred to [Github API docs](https://docs.github.com/en/rest).


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


## Available Options (2020-08-05 updated)
> Option

- -l  :  language (java, c, cpp, javascript, ruby, etc.)  <br />

- -f  :  fork count (e.g., >=500, 10..200)  <br />

- -d  :  last pushed date (same usage as above)  <br /><br />

- -c  :  created date (same usage as above)  <br /><br />

- -cb :  (optional) commit count (criteria of commit count, same usage as above)  <br /><br />

- -auth : (optional) authentication token for Github user. (It allows to send more requests per minute.)  <br /><br /> 



## About commit option
Users can give criteria for the total number of commits in the last year of each repository. 
However, since no services related to commit are provided in the repository searching query, 
the system will once again compute the list of discovered. Unfortunately, this may take a long time to create a list of thousands of repositories 
that meets the criteria. The commit option is certainly important, but it can be used when necessary because it can give results quickly when not in use. <br /><br />



## Example
```linux
AllGitClone -l java -f 100..2000 -d 2019-01-01..2020-01-01 -auth 'personal_token'  
```

This means that we can get a list of repositories, written in java, have fork counts greater than 100 and less than 2000,  <br />
and whose last committed date is 2019-01-01 to 2020-01-01, using personal authentication token.


```linux
AllGitClone -l cpp -f >=50 -d >=2020-07-15 -cb > 50
```

This means that we can get a list of repositories, written in C++, have forks counts  greater than 50, <br />
and have commmits greater than 50, without any token.
<br />
