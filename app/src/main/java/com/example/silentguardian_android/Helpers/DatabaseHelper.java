package com.example.silentguardian_android.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private Context context;

    private static final String TAG = "DatabaseHelper";

    //Due to this constructor I have made two database helpers
    //However It could be done with one
    public DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);

        this.context = context;
    }

    //function is called when database is created for the first time
    //wont be called again
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String CREATE_TABLE_COURSE = "CREATE TABLE " + Config.COURSE_TABLE_NAME + " (" + Config.COLUMN_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_PERSON_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_PERSON_NUMBER + " TEXT NOT NULL, "
                + Config.COLUMN_PERSON_THESHOLD_ONE + " TEXT NOT NULL, "
                + Config.COLUMN_PERSON_THESHOLD_TWO + " TEXT NOT NULL)";

        Log.d(TAG, CREATE_TABLE_COURSE);

        sqLiteDatabase.execSQL(CREATE_TABLE_COURSE);

        Log.d(TAG, "db created");

    }

    //if structure changes we don't want to recreate because data will be lost
    //alters old DB
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public long insertPerson(Person person) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Config.COLUMN_PERSON_NAME, person.getName());
        contentValues.put(Config.COLUMN_PERSON_NUMBER, person.getPhoneNumber());
        contentValues.put(Config.COLUMN_PERSON_THESHOLD_ONE, person.getThresholdOne());
        contentValues.put(Config.COLUMN_PERSON_THESHOLD_TWO, person.getThresholdTwo());
        ///threshold value is intalized to zero

        try {

            id = db.insertOrThrow(Config.COURSE_TABLE_NAME, null, contentValues); //will create autoincremented ID and return it

        } catch (SQLiteException e) {
            Log.d(TAG, "Execption: " + e);
            Toast.makeText(context, "Operation failed" + e, Toast.LENGTH_LONG).show();

        } finally //if everything works fine this will execute or not
        {
            db.close();
        }
        return id;
    }

    ///need a function to set the threshold value for a person

    //and another to return all the people assocated with a value

    public List<Person> getAllPeople() {
        SQLiteDatabase db = this.getReadableDatabase(); //open database in readmode
        Cursor cursor = null;

        try {
            cursor = db.query(Config.COURSE_TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    List<Person> people = new ArrayList<>();

                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERSON_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_NAME));
                        String phone = cursor.getString(cursor.getColumnIndex((Config.COLUMN_PERSON_NUMBER)));
                        int thresholdOne = cursor.getInt(cursor.getColumnIndex((Config.COLUMN_PERSON_THESHOLD_ONE)));
                        int thresholdTwo = cursor.getInt(cursor.getColumnIndex((Config.COLUMN_PERSON_THESHOLD_TWO)));
                        people.add(new Person(id, name, phone, thresholdOne, thresholdTwo)); //makes a new course and add its to the list
                    } while (cursor.moveToNext());

                    return people;
                }
            }
        } catch (SQLException e) {

            Log.d(TAG, "EXCEPTION" + e);
            Toast.makeText(context, "OPERATION FAILED: " + e, Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return Collections.emptyList();
    }

    ///getting people within the thresholdOne
    public List<Person> getThresholdOne() {
        SQLiteDatabase db = this.getReadableDatabase(); //open database in readmode
        Cursor cursor = null;

        try {
            cursor = db.query(Config.COURSE_TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    List<Person> people = new ArrayList<>();

                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERSON_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_NAME));
                        String phone = cursor.getString(cursor.getColumnIndex((Config.COLUMN_PERSON_NUMBER)));
                        int thresholdOne = cursor.getInt(cursor.getColumnIndex((Config.COLUMN_PERSON_THESHOLD_ONE)));
                        int thresholdTwo = cursor.getInt(cursor.getColumnIndex((Config.COLUMN_PERSON_THESHOLD_TWO)));

                        if (thresholdOne == 1) {
                            people.add(new Person(id, name, phone, thresholdOne, thresholdTwo)); //makes a new course and add its to the list
                        }
                    } while (cursor.moveToNext());

                    return people;
                }
            }
        } catch (SQLException e) {

            Log.d(TAG, "EXCEPTION" + e);
            Toast.makeText(context, "OPERATION FAILED: " + e, Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return Collections.emptyList();
    }


    ///getting people within thresholdTwo
    public List<Person> getThresholdTwo() {
        SQLiteDatabase db = this.getReadableDatabase(); //open database in readmode
        Cursor cursor = null;

        try {
            cursor = db.query(Config.COURSE_TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    List<Person> people = new ArrayList<>();

                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERSON_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_NAME));
                        String phone = cursor.getString(cursor.getColumnIndex((Config.COLUMN_PERSON_NUMBER)));
                        int thresholdOne = cursor.getInt(cursor.getColumnIndex((Config.COLUMN_PERSON_THESHOLD_ONE)));
                        int thresholdTwo = cursor.getInt(cursor.getColumnIndex((Config.COLUMN_PERSON_THESHOLD_TWO)));

                        if (thresholdTwo == 1) {
                            people.add(new Person(id, name, phone, thresholdOne, thresholdTwo)); //makes a new course and add its to the list
                        }
                    } while (cursor.moveToNext());

                    return people;
                }
            }
        } catch (SQLException e) {

            Log.d(TAG, "EXCEPTION" + e);
            Toast.makeText(context, "OPERATION FAILED: " + e, Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return Collections.emptyList();
    }


    //deletes a course from the database tables
    public void deletePerson(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("__dbHelper", "Db delete: " +
                db.delete(Config.COURSE_TABLE_NAME, Config.COLUMN_PERSON_ID + " = ?",
                        new String[]{String.valueOf(id)}));

    }

    public void updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PERSON_ID, person.getID());
        contentValues.put(Config.COLUMN_PERSON_NAME, person.getName());
        contentValues.put(Config.COLUMN_PERSON_NUMBER, person.getPhoneNumber());
        contentValues.put(Config.COLUMN_PERSON_THESHOLD_ONE, person.getThresholdOne());
        contentValues.put(Config.COLUMN_PERSON_THESHOLD_TWO, person.getThresholdTwo());


        db.update(Config.COURSE_TABLE_NAME, contentValues, Config.COLUMN_PERSON_ID + " = ?", new String[]{String.valueOf(person.getID())});
    }

    public boolean checkifEmpty() {

        SQLiteDatabase db = this.getReadableDatabase(); //open database in readmode

        Cursor mCursor = db.query(Config.COURSE_TABLE_NAME, null, null, null, null, null, null);
        Boolean rowExists;

        if (mCursor.moveToFirst()) {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

        } else {
            // I AM EMPTY
            rowExists = false;
        }

        return rowExists;
    }

}
