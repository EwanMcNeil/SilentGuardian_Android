package com.example.silentguardian_android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.silentguardian_android.Database.AudioDatabase;
import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.Database.SharePreferenceHelper;
import com.example.silentguardian_android.Database.audioFile;
import com.example.silentguardian_android.fragments.deleteContactFromThresholdFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;


public class AudioRecordTest extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    protected SharePreferenceHelper sharePreferenceHelper;

    private Button recordButton = null;
    private MediaRecorder recorder = null;


    private MediaPlayer   player = null;
    boolean mStartPlaying = true;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    boolean mStartRecording = true;

    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private ListView files;
    private AudioDatabase adb;
    List<audioFile> AllaudioFiles;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }



    private void startPlaying(String input) {
        player = new MediaPlayer();
        try {
            player.setDataSource(input);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        int num = adb.numberAudioObjects();
        num = num +1;
        String newfilename = fileName + "/audiorecordtest" + num + ".3gp";
        Date currentTime = Calendar.getInstance().getTime();
        String date = currentTime.toString();
        audioFile file = new audioFile(date,newfilename);
        adb.insertFile(file);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(newfilename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.i("tag", "This'll run 5000 milliseconds later");
                        stopRecording();
                        recordButton.setText("Start recording");
                    }
                },
                5000);
    }

    private void stopRecording() {
        if(recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            loadListView();
        }
    }



    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.audioactivity);
        files = findViewById(R.id.audioListView);
        adb = new AudioDatabase(this);
        //Next lines assure activity uses the right language, otherwise some activities or fragment aren't fully catching up
        //Applying language start
        sharePreferenceHelper = new SharePreferenceHelper(this);
        String language = sharePreferenceHelper.languageReturn();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //Applying language end



        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();


        //fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        recordButton = findViewById(R.id.recordbutton);


        files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String fileLocation = AllaudioFiles.get(position).getFile();
                startPlaying(fileLocation);

            }
        });





        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    recordButton.setText("Stop recording");
                } else {
                    recordButton.setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }

        });


        loadListView();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }


    public void loadListView() {

       AudioDatabase dbhelper = new AudioDatabase(this);
        List<audioFile> audioFiles = dbhelper.getAllfiles();
        AllaudioFiles = audioFiles;
        ArrayList<String> fileListText = new ArrayList<>();

        for (int i = 0; i < audioFiles.size(); i++) {
            String temp = "";
            temp += audioFiles.get(i).getDate() + '\n';
            //temp += audioFiles.get(i).getFile() + '\n';

            fileListText.add(temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,  fileListText);

       files.setAdapter(arrayAdapter);

    }

 
}