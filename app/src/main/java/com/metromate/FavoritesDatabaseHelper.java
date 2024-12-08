package com.metromate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.libraries.mapsplatform.transportation.consumer.model.Route;

import java.util.ArrayList;

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MetroMate.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FAVORITES = "favorites";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_START = "start_point";
    private static final String COLUMN_END = "end_point";

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "start_point TEXT, " +  // COLUMN_START
                "end_point TEXT)";      // COLUMN_END
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // 즐겨찾기 추가
    public void addFavorite(String start, String end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_START, start);
        values.put(COLUMN_END, end);

        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    // 모든 즐겨찾기 조회
    public ArrayList<Route> getAllFavorites() {
        ArrayList<Route> routes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVORITES, null);

        if (cursor.moveToFirst()) {
            do {
                String start = cursor.getString(cursor.getColumnIndex(COLUMN_START));
                String end = cursor.getString(cursor.getColumnIndex(COLUMN_END));
                routes.add(new Route(start, end));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return routes;
    }

    // 즐겨찾기 삭제
    public void deleteFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
