package com.prabhat.affirmer7;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by prabhat on 17/10/13.
 * Need to implement onClickListener else events will be routed to main activity class instead of fragment class
 */
public class FragmentRecorder extends Fragment implements View.OnClickListener
{
    public static final String ARG_OBJECT = "object";

    boolean bPlaying=false;
    boolean bRecording=false;
    final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    String sLastFileName="";

    Button btnStartStopRecording;
    Button btnStartStopPlaying;

    MediaRecorder mRecorder=null;
    MediaPlayer mPlayer=null;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_recorder,container,false);

        btnStartStopRecording=(Button)view.findViewById(R.id.btnStartStopRecording);
        btnStartStopRecording.setOnClickListener(this);

        btnStartStopPlaying=(Button)view.findViewById(R.id.btnStartStopPlaying);
        btnStartStopPlaying.setOnClickListener(this);
        btnStartStopPlaying.setEnabled(false);

        return(view);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(mRecorder!=null)
        {
            mRecorder.stop();
            mRecorder.release();
            mRecorder=null;
        }
        if(mPlayer!=null)
        {
            mPlayer.stop();
            mPlayer.release();
            mPlayer=null;
        }
        btnStartStopPlaying.setEnabled(false);
        btnStartStopPlaying.setText("Start Playing");
        btnStartStopRecording.setEnabled(true);
        btnStartStopRecording.setText("Start Recording");

        Log.e("FragmentRecorder.onPause()" , "RecorderFragment paused");
    }


    public void StartStopRecording()
    {
        if(bRecording)
        {
            btnStartStopRecording.setText("Start Recording");
            btnStartStopPlaying.setEnabled(true);
            bRecording=false;
            stopRecording();
        }
        else
        {
            btnStartStopRecording.setText("Stop Recording");
            btnStartStopPlaying.setEnabled(false);
            bRecording=true;
            startRecording();
        }
    }

    public void startRecording()
    {
        String sNewFile=getFilename();
        Log.e("startRecording()",sNewFile);

        try {
            mRecorder=new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.setOutputFile(sNewFile);
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("startRecording()",e.toString());
        }
    }

    public void stopRecording() {
        if (null != mRecorder)
        {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            //Log.e("stopRecording()","Last file");
        }
    }


    public void StartStopPlaying()
    {
        if(bPlaying)
        {
            btnStartStopPlaying.setText("Start Playing");
            btnStartStopRecording.setEnabled(true);
            bPlaying=false;
            stopPlaying();
            //Log.e("StartStopPlaying()","stopped playing");
        }
        else
        {
            btnStartStopPlaying.setText("Stop Playing");
            btnStartStopRecording.setEnabled(false);
            bPlaying=true;
            startPlaying();
            //Log.e("StartStopPlaying()","started playing");
        }
    }

    public void startPlaying()
    {
        //set up MediaPlayer
        try {
            //Log.e("startPlaying()","setting data source: " + sLastFileName);
            mPlayer=new MediaPlayer();
            mPlayer.setLooping(true);
            mPlayer.setDataSource(sLastFileName);
            mPlayer.prepare();
            mPlayer.start();
            //Log.e("startPlaying()","started playing");
        } catch (IllegalStateException e) { e.printStackTrace();    }
        catch (IOException ioe) {    ioe.printStackTrace();         }
        catch(Exception e)      {    e.printStackTrace();           }
    }

    public void stopPlaying()
    {
        mPlayer.stop();
        mPlayer.release();
        //Log.e("stopPlaying()","stopped playing");
    }

    private String getFilename() {
        String filename;
        DateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        filename= dateFormat.format(new Date());

        String filePath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filePath, AUDIO_RECORDER_FOLDER);

        sLastFileName=file.getAbsolutePath() + "/" + filename + ".mp4";
        return (file.getAbsolutePath() + "/" + filename + ".mp4");
    }

    /*
        Need to implement onClick, else the events will be routed to main activity class instead of fragment class
        Do not try to use android:onClick as it will send the events
     */
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnStartStopRecording: StartStopRecording(); break;
            case R.id.btnStartStopPlaying: StartStopPlaying();break;
        }
    }
}