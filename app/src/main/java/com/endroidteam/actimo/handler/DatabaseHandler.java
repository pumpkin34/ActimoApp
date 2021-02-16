package com.endroidteam.actimo.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.endroidteam.actimo.model.Users;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "usersManager";

    private static final String TABLE_USERS = "users";

    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PWD = "password";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_PWD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    public int addUsers(Users users) {
        SQLiteDatabase db = this.getWritableDatabase();
        int okay;

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, users.getUsername());
        values.put(KEY_PWD, users.getPassword());

        db.insert(TABLE_USERS, null, values);

        if (db.isDatabaseIntegrityOk()) {
            okay = 0;
            db.close();
        } else {
            okay = 1;
        }

        return okay;
    }

    public int getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int found;

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID,
                        KEY_USERNAME, KEY_PWD}, KEY_USERNAME + "=?" + " AND " + KEY_PWD + "=?",
                new String[]{String.valueOf(username), String.valueOf(password)}, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            cursor.close();
            found = 0;
        } else {
            found = -1;
        }

        return found;
    }
}