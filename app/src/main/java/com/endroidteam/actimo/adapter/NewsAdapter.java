package com.endroidteam.actimo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.endroidteam.actimo.R;
import com.endroidteam.actimo.model.Articles;

import java.util.List;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<Articles> articles;
    private Context context;
    private String getHeader, getURL;

    public NewsAdapter(List<Articles> articlesList, Context context) {
        this.articles = articlesList;
        this.context = context;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.news_item_card, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Articles articlesNode = articles.get(position);

        holder.title.setText(articlesNode.getTitle());
        holder.publishedAt.setText(articlesNode.getPublishedAt());
        holder.author.setText(articlesNode.getAuthor());
        holder.description.setText(articlesNode.getDescription());

        Glide.with(holder.urlToImage.getContext())
                .load(articlesNode.getUrlToImage())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        holder.loadingPanel.setVisibility(View.GONE);
                        holder.urlToImage.setImageDrawable(resource);
                    }
                });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.overflow, holder.title.getText().toString(), articlesNode.getUrl());
            }
        });
    }

    private void showPopupMenu(View view, String header, String url) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(header, url));
        popup.show();
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        MyMenuItemClickListener(final String newsHeader, String newsUrl) {
            getHeader = newsHeader;
            getURL = newsUrl;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.share_quickly:
                    String packageName = context.getPackageName();

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, ("'" + getHeader + "'" + " " + context.getString(R.string.overflow_news_text) + " " + "http://play.google.com/store/apps/details?id=" + packageName));
                    if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_with_that)));
                    }
                    return true;
                case R.id.open_in_browser_quickly:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(getURL));
                    if (browserIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(browserIntent);
                    }
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, publishedAt, description, author;
        private ImageView urlToImage, overflow;
        private RelativeLayout loadingPanel;

        ViewHolder(View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.author);
            title = (TextView) itemView.findViewById(R.id.title);
            publishedAt = (TextView) itemView.findViewById(R.id.publishedAt);
            urlToImage = (ImageView) itemView.findViewById(R.id.urlToImage);
            description = (TextView) itemView.findViewById(R.id.description);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);
            loadingPanel = (RelativeLayout) itemView.findViewById(R.id.loadingPanel);
        }
    }
}
