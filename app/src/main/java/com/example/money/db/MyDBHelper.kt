package com.example.money.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyDBHelper(context: Context): SQLiteOpenHelper(context,"USERDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE \"settings\" (" +
                "\"month_limit\" INTEGER, " +
                "\"currency_index\" INTEGER, " +
                "\"fav_category_index\" INTEGER, "+
                "\"enabled_sound_effects\" INTEGER, "+
                "\"rating\" REAL);")

        db?.execSQL("CREATE TABLE \"purchases\" (" +
                "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "\"date\" TEXT NOT NULL, " +
                "\"place\" TEXT NOT NULL, " +
                "\"amount\" INTEGER NOT NULL, " +
                "\"category\" INTEGER NOT NULL\n);")

        db?.execSQL("INSERT INTO settings(" +
                "month_limit," +
                "currency_index, " +
                "fav_category_index, " +
                "enabled_sound_effects, " +
                "rating) " +
                "VALUES(0, 0, 0, 1, 0.0)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}