package co.edu.unal.jdporrasp.devcompanyfinder.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CompanyDBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "companies.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_COMPANIES = "companies";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PRODUCTS_AND_SERVICES = "products_and_services";
    public static final String COLUMN_CATEGORY = "category";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_COMPANIES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_WEBSITE + " TEXT, " +
                    COLUMN_PHONE_NUMBER + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PRODUCTS_AND_SERVICES + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT " +
                    ")";

    public CompanyDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANIES);
        db.execSQL(TABLE_CREATE);
    }
}
