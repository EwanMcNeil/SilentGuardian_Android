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

public class AudioDatabase extends SQLiteOpenHelper {



    private Context context;

    private static final String TAG = "AudioDatabase";

    //Due to this constructor I have made two database helpers
    //However It could be done with one
    public AudioDatabase(Context context) {
        super(context, AudioConfig.DATABASE_NAME, null, AudioConfig.DATABASE_VERSION);

        this.context = context;
    }

    //function is called when database is created for the first time
    //wont be called again
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String CREATE_TABLE_COURSE = "CREATE TABLE " + AudioConfig.TABLE_NAME + " (" + AudioConfig.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AudioConfig.COLUMN_DATE + " TEXT NOT NULL, "
                + AudioConfig.COLUMN_FILENAME + " TEXT NOT NULL)";


        Log.d(TAG, CREATE_TABLE_COURSE);

        sqLiteDatabase.execSQL(CREATE_TABLE_COURSE);

        Log.d(TAG, "audio db created");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public long insertFile(audioFile file)
    {
        Log.d(TAG, "insertFile");
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(AudioConfig.COLUMN_DATE, file.getDate());
        contentValues.put(AudioConfig.COLUMN_FILENAME, file.getFile());

        ///threshold value is intalized to zero

        try{

            id = db.insertOrThrow(AudioConfig.TABLE_NAME,null, contentValues); //will create autoincremented ID and return it

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

    public List<audioFile> getAllfiles()
    {
        SQLiteDatabase db = this.getReadableDatabase(); //open database in readmode
        Cursor cursor = null;
        Log.d(TAG, "getallfiles");

        try {
            cursor = db.query(AudioConfig.TABLE_NAME, null, null, null, null, null, null);

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    java.util.List<com.example.silentguardian_android.Helpers.audioFile> audioFiles = new ArrayList<>();

                    do{
                        int id = cursor.getInt(cursor.getColumnIndex(AudioConfig.COLUMN_ID));
                        String Date = cursor.getString(cursor.getColumnIndex(AudioConfig.COLUMN_DATE));
                        String File = cursor.getString(cursor.getColumnIndex((AudioConfig.COLUMN_FILENAME)));
                        audioFiles.add(new com.example.silentguardian_android.Helpers.audioFile(id,Date,File)); //makes a new course and add its to the list
                    }while(cursor.moveToNext());

                    return audioFiles;
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


    public int numberAudioObjects(){
        int output = 0;
        SQLiteDatabase db = this.getReadableDatabase(); //open database in readmode
        Cursor cursor = null;

        Log.d(TAG, "getlength");

        try {
            cursor = db.query(AudioConfig.TABLE_NAME, null, null, null, null, null, null);

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {


                    do{
                       output++;
                    }while(cursor.moveToNext());

                    return output;
                }
            }
        }
        catch (SQLException e){

            Log.d(TAG, "EXCEPTION" + e);
            Toast.makeText(context, "countfailed: " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
            db.close();
        }

        return 0;

    }
}
