package com.endroidteam.actimo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.endroidteam.actimo.R;
import com.endroidteam.actimo.utils.RecyclerViewBuilder;

public class NewsActivity extends AppCompatActivity {

    TextView tvUsername;
    Context context = this;
    final String SP_TAG="Actimo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUsername.setText("Welcome, " + getIntent().getStringExtra("username") + " !");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerViewBuilder recyclerViewBuilder = new RecyclerViewBuilder();
        recyclerViewBuilder.initNewsRV(getIntent().getStringExtra("news_res"),recyclerView, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Pick the news resource");
                builder.setItems(R.array.News_Resources, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences(SP_TAG, MODE_PRIVATE).edit();
                        editor.putString("news_resource", getResources().getStringArray(R.array.News_Links)[which]);
                        editor.apply();

                        Toast.makeText(context, "You chose " + getResources().getStringArray(R.array.News_Resources)[which] + " as a news source. Changes will be reflected after restarting the app.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();

            default:
                return true;
        }
    }
}
