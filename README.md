# AllGitClone
A tool for cloning git repositories

Rest API using Retrofit + Okhttp

Need to implement
- Can I get all repositories in github?
- Can I give some options to get repositories?
- The methods that apply proper options.
  - ```java JsonObject jsonObject = new Gson().fromJson(response.body().get(i), JsonObject.class);```
  - query to search (ex: api.github.com/search/repositories?q=language:java)

- First total search, and options to java, or set firstly query options?


I. get all data (like, repository name, created timestamp, repository url, repository description...) using retrofit

II. apply options using query (like, language, timestamp, commit counts, fork counts...)

III. design to command line program

IV. replace all options to command line

