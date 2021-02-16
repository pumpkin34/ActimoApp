package com.endroidteam.actimo.utils;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.endroidteam.actimo.adapter.NewsAdapter;
import com.endroidteam.actimo.model.Articles;
import com.endroidteam.actimo.providers.FetchData;

import java.util.ArrayList;
import java.util.List;



public class RecyclerViewBuilder {
    public void initNewsRV(String URL, RecyclerView recyclerView, final Context context) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        final List<Articles> articlesList = new ArrayList<>();
        final RecyclerView.Adapter adapter = new NewsAdapter(articlesList, context);
        recyclerView.setAdapter(adapter);


        final FetchData fetchData = new FetchData();
        fetchData.fetch(context, articlesList, adapter, URL);
    }
}
