package co.edu.unal.jdporrasp.devcompanyfinder.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.edu.unal.jdporrasp.devcompanyfinder.model.Company;

public class CompanyOperations {
    public static final String LOGTAG = "COMP_MNGMT_SYS";

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    private static final String[] allColumns = {
        CompanyDBHandler.COLUMN_ID,
        CompanyDBHandler.COLUMN_NAME,
        CompanyDBHandler.COLUMN_WEBSITE,
        CompanyDBHandler.COLUMN_PHONE_NUMBER,
        CompanyDBHandler.COLUMN_EMAIL,
        CompanyDBHandler.COLUMN_PRODUCTS_AND_SERVICES,
        CompanyDBHandler.COLUMN_CATEGORY
    };

    public CompanyOperations(Context context){
        dbHandler = new CompanyDBHandler(context);
    }

    public void open(){
        Log.i(LOGTAG, "Database Opened");
        db = dbHandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbHandler.close();
    }

    public Company addCompany(Company company)
    {
        ContentValues values = new ContentValues();
        values.put(CompanyDBHandler.COLUMN_NAME, company.getName());
        values.put(CompanyDBHandler.COLUMN_WEBSITE, company.getWebsite());
        values.put(CompanyDBHandler.COLUMN_PHONE_NUMBER, company.getPhoneNumber());
        values.put(CompanyDBHandler.COLUMN_EMAIL, company.getEmail());
        values.put(CompanyDBHandler.COLUMN_PRODUCTS_AND_SERVICES, company.getProductsAndServices());
        values.put(CompanyDBHandler.COLUMN_CATEGORY, company.getCategory());
        long insertId = db.insert(CompanyDBHandler.TABLE_COMPANIES, null, values);
        company.setId(insertId);
        return company;
    }

    public Company getCompany(long id) {
        //Cursor cs = database.query(EmployeeDBHandler.TABLE_EMPLOYEES,allColumns,EmployeeDBHandler.COLUMN_ID + "=?",new String[]{String.valueOf(id)},null,null, null, null)
        Cursor cursor = db.query(
                CompanyDBHandler.TABLE_COMPANIES,
                allColumns,
                CompanyDBHandler.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //Employee e = new Employee(Long.parseLong(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));

        return new Company(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
        );
    }

    public List<Company> getAllCompanies() {
        //Cursor cursor = database.query(EmployeeDBHandler.TABLE_EMPLOYEES,allColumns,null,null,null, null, null);
        Cursor cursor = db.query(CompanyDBHandler.TABLE_COMPANIES, allColumns, null, null, null, null, null);

        List<Company> companies = new ArrayList<>();
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                Company company = new Company();
                company.setId(cursor.getLong(cursor.getColumnIndex(CompanyDBHandler.COLUMN_ID)));
                company.setName(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_NAME)));
                company.setWebsite(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_WEBSITE)));
                company.setEmail(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_EMAIL)));
                company.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_PHONE_NUMBER)));
                company.setProductsAndServices(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_PRODUCTS_AND_SERVICES)));
                company.setCategory(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_CATEGORY)));
                companies.add(company);
            }
        }

        return companies;
    }

    public int updateCompany(Company company){
        ContentValues values = new ContentValues();

        values.put(CompanyDBHandler.COLUMN_NAME, company.getName());
        values.put(CompanyDBHandler.COLUMN_WEBSITE, company.getWebsite());
        values.put(CompanyDBHandler.COLUMN_PHONE_NUMBER, company.getPhoneNumber());
        values.put(CompanyDBHandler.COLUMN_EMAIL, company.getEmail());
        values.put(CompanyDBHandler.COLUMN_PRODUCTS_AND_SERVICES, company.getProductsAndServices());
        values.put(CompanyDBHandler.COLUMN_CATEGORY, company.getCategory());

        return db.update(CompanyDBHandler.TABLE_COMPANIES, values,
                CompanyDBHandler.COLUMN_ID + "=?", new String[] {String.valueOf(company.getId())});
    }

    public void removeCompany(Company company){
        db.delete(CompanyDBHandler.TABLE_COMPANIES, CompanyDBHandler.COLUMN_ID + "=" + company.getId(), null);
    }
}
