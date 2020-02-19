package com.example.silentguardian_android.Database;

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

import androidx.annotation.Nullable;

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


        String CREATE_TABLE_COURSE = "CREATE TABLE " + Config.COURSE_TABLE_NAME + " (" + Config.COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_COURSE_TITLE + " TEXT NOT NULL, "
                + Config.Column_COURSE_CODE + " TEXT NOT NULL)";

        Log.d(TAG, CREATE_TABLE_COURSE);

        sqLiteDatabase.execSQL(CREATE_TABLE_COURSE);

        Log.d(TAG, "db created");

    }

    //if structure changes we don't want to recreate because data will be lost
    //alters old DB
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



    public long insertCourse(Course course)
    {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Config.COLUMN_COURSE_TITLE, course.getTitle());
        contentValues.put(Config.Column_COURSE_CODE, course.getCode());

        try{

            id = db.insertOrThrow(Config.COURSE_TABLE_NAME,null, contentValues); //will create autoincremented ID and return it

        }
        catch(SQLiteException e)
        {
            Log.d(TAG, "Execption: " + e);
            Toast.makeText(context, "Operation failed" + e, Toast.LENGTH_LONG).show();

        }
        finally //if everything works fine this will execute or not
        {
            db.close();
        }
        return id;
    }

    public List<Course> getAllCourses()
    {
        SQLiteDatabase db = this.getReadableDatabase(); //open database in readmode
        Cursor cursor = null;

        try {
            cursor = db.query(Config.COURSE_TABLE_NAME, null, null, null, null, null, null);

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    List<Course> courses = new ArrayList<>();

                    do{
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_COURSE_ID));
                        String title = cursor.getString(cursor.getColumnIndex(Config.COLUMN_COURSE_TITLE));
                        String code = cursor.getString(cursor.getColumnIndex((Config.Column_COURSE_CODE)));

                        courses.add(new Course(id,title,code)); //makes a new course and add its to the list
                    }while(cursor.moveToNext());

                    return courses;
                }
            }
        }
        catch (SQLException e){

            Log.d(TAG, "EXCEPTION" + e);
            Toast.makeText(context, "OPERATION FAILED: " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
            db.close();
        }

        return Collections.emptyList();
    }

    //deletes a course from the database tables
    public void deleteCourse(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(Config.COURSE_TABLE_NAME,Config.COLUMN_COURSE_ID+ " = ?",
                new String[] { String.valueOf(id)});

    }

}
