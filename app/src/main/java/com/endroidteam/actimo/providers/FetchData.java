package com.endroidteam.actimo.providers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.endroidteam.actimo.model.Articles;
import com.endroidteam.actimo.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class FetchData {
    public void fetch(final Context context, final List<Articles> articlesList, final RecyclerView.Adapter adapter, String URL)

    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final NewsService newsService = retrofit.create(NewsService.class);
        Call<News> newsCall = newsService.newsNode("articles?source=" + URL +"&sortBy=top&apiKey=6694b16ba6fe413bb4c0ddb8d926ef43");


        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()) {
                    News news = response.body();
                    Articles articles = null;

                    try {
                        for (int i = 0; i < news.articles.size(); i++) {
                            articles = new Articles();
                            articles = news.articles.get(i);

                            articles.setPublishedAt(articles.publishedAt);
                            articles.setAuthor(articles.author);
                            articles.setUrlToImage(articles.urlToImage);
                            articles.setTitle(articles.title);
                            articles.setDescription(articles.description);
                            articles.setUrl(articles.url);

                            articlesList.add(articles);
                            adapter.notifyDataSetChanged();

                        }
                    } catch (Exception ignored) {
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(context, "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
