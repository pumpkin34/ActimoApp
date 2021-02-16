package com.endroidteam.actimo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import com.endroidteam.actimo.activities.NewsActivity;
import com.endroidteam.actimo.handler.DatabaseHandler;
import com.endroidteam.actimo.model.Users;

import shem.com.materiallogin.DefaultLoginView;
import shem.com.materiallogin.DefaultRegisterView;
import shem.com.materiallogin.MaterialLoginView;

public class MainActivity extends AppCompatActivity {

    public DatabaseHandler databaseHandler;
    final String SP_TAG = "Actimo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHandler = new DatabaseHandler(this);

        final MaterialLoginView login = (MaterialLoginView) findViewById(R.id.login);

        //region Login
        ((DefaultLoginView) login.getLoginView()).setListener(new DefaultLoginView.DefaultLoginViewListener() {

            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
                final String user = loginUser.getEditText().getText().toString();
                if (user.isEmpty()) {
                    loginUser.setError("Username can't be empty");
                    return;
                }
                loginUser.setError("");

                String pass = loginPass.getEditText().getText().toString();
                if (pass.isEmpty()) {
                    loginPass.setError("Password can't be empty");
                    return;
                }
                loginPass.setError("");

                if (isNetworkAvailable()) {
                    if (getUser(user, pass) == 0) {
                        Snackbar.make(login, "Login success! Opening the app...", Snackbar.LENGTH_SHORT).show();

                        SharedPreferences preferences = getSharedPreferences(SP_TAG, MODE_PRIVATE);
                        final String news_resource = preferences.getString("news_resource", "bbc-news");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent newsIntent = new Intent(MainActivity.this, NewsActivity.class);
                                newsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                newsIntent.putExtra("username", user);
                                newsIntent.putExtra("news_res", news_resource);
                                startActivity(newsIntent);
                            }
                        }, 1000);

                    } else {
                        Snackbar.make(login, "Please check your username and password!", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(login, "Please check your network connection!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //endregion

        //region Register
        ((DefaultRegisterView) login.getRegisterView()).setListener(new DefaultRegisterView.DefaultRegisterViewListener() {
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                String user = registerUser.getEditText().getText().toString();
                if (user.isEmpty()) {
                    registerUser.setError("Username can't be empty");
                    return;
                }
                registerUser.setError("");

                String pass = registerPass.getEditText().getText().toString();
                if (pass.isEmpty()) {
                    registerPass.setError("Password can't be empty");
                    return;
                }
                registerPass.setError("");

                String passRep = registerPassRep.getEditText().getText().toString();
                if (!pass.equals(passRep)) {
                    registerPassRep.setError("Passwords are different");
                    return;
                }
                registerPassRep.setError("");

                if (registerUser(user, pass) == 0) {
                    Snackbar.make(login, "Register success!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(login, "Register failed! Please try again...", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //endregion
    }

    public int registerUser(String username, String pwd) {
        return databaseHandler.addUsers(new Users(username, pwd));
    }

    public int getUser(String username, String pwd) {
        return databaseHandler.getUser(username, pwd);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
