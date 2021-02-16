package com.endroidteam.actimo.providers;

import com.endroidteam.actimo.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;



interface NewsService {
    String BASE_URL = "https://newsapi.org/v1/";

    @GET
    Call<News> newsNode(@Url String url);
}
