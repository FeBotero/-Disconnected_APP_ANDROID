package com.example.disconected;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "api_data.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela para armazenar os dados da API
    public static final String TABLE_API_DATA = "api_data";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_IS_ACTIVE = "is_active";
    public static final String COLUMN_PASSWORD = "password";

    private static final String CREATE_TABLE_API_DATA =
            "CREATE TABLE " + TABLE_API_DATA + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_IS_ACTIVE + " INTEGER NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT" +
                    ");";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_API_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_API_DATA);
        onCreate(db);
    }

    public boolean saveEmailToDatabase(String email) {
        try {
            SQLiteDatabase db = this.getWritableDatabase(); // Use this.getWritableDatabase()
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_EMAIL, email);
            values.put(DatabaseHelper.COLUMN_IS_ACTIVE, 1); // Assuming 1 means active
            // You can add more data to be inserted if needed.

            long newRowId = db.insert(DatabaseHelper.TABLE_API_DATA, null, values);
            return newRowId != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
