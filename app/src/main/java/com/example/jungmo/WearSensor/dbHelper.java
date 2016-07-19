package com.example.jungmo.WearSensor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jungmo on 7/16/16.
 */
public class dbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wearable.db";
    private static final int DATABASE_VERSION = 2;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sensor (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "time TEXT, " +
                "acc_x TEXT, " +
                "acc_y TEXT, " +
                "acc_z TEXT, " +
                "alpha TEXT, " +
                "beta TEXT, " +
                "gamma TEXT, " +
                "hr TEXT);");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sensors");
        onCreate(db);
    }
}
