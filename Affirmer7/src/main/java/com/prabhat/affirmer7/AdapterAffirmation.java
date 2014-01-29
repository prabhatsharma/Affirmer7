package com.prabhat.affirmer7;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by prabhat on 25/1/14. A lot of work is happening in this adapter
 */
public class AdapterAffirmation extends ArrayAdapter <Affirmation>
{
    Context context;
    int resource;
    ArrayList<Affirmation> data = null;
    ImageView imgPlayPause;
    ImageView imgDelete;
    TextView txtFilename;

    View lastView;
    int lastPosition=-1;
    int currentPosition=-1;

    String filePath;

    MediaPlayer mPlayer;

    ViewGroup parent;

    final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

    boolean bPlay=false;

    int screenWidth;

    AdapterAffirmation(Context context, int resource, ArrayList<Affirmation> data)
    {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        if(Build.VERSION.SDK_INT>=13)
        {
            display.getSize(size);
            screenWidth = size.x;
        }
        else
        {
            //noinspection deprecation
            screenWidth = display.getWidth();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.single_affirmation,parent,false);
        this.parent=parent;

        imgPlayPause=(ImageView)convertView.findViewById(R.id.imgPlayPause);
        imgPlayPause.setOnClickListener(AffirmationListener);

        imgDelete=(ImageView)convertView.findViewById(R.id.imgDelete);
        imgDelete.setOnClickListener(AffirmationListener);

        txtFilename=(TextView)convertView.findViewById(R.id.txtSingleAffirmation);
        txtFilename.setText(data.get(position).toString());
        txtFilename.setOnClickListener(AffirmationListener);

        imgDelete.setTag(position);
        imgDelete.setTag(R.string.file_name,txtFilename.getText().toString());

        txtFilename.setTag(position);
        txtFilename.setTag(R.string.file_name,txtFilename.getText().toString());

        imgPlayPause.setTag(position);
        imgPlayPause.setTag(R.string.files_count,getCount());
        imgPlayPause.setTag(R.string.file_name,txtFilename.getText());


        filePath = Environment.getExternalStorageDirectory().getPath() +"/" + AUDIO_RECORDER_FOLDER;


        txtFilename.setWidth(screenWidth - 300);

        return convertView;
    }



    private View.OnClickListener AffirmationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.imgPlayPause:
                    imgPlayPauseChanger((ImageView)v);
                    lastView=v;
                    lastPosition=Integer.parseInt(v.getTag().toString());
                    break;
                case R.id.imgDelete:        imgDeleteAction((ImageView)v);      break;
                case R.id.txtSingleAffirmation: renameFile((TextView)v);        break;
            }
        }
    };

    public void renameFile(TextView textView) {

        //textView.setBackgroundColor(0xFFFFFF);

        final String theFile=textView.getTag(R.string.file_name).toString();
        final File file=new File(filePath + "/" + theFile);

        AlertDialog.Builder alert = new AlertDialog.Builder(textView.getContext());

        alert.setTitle("Title");
        alert.setMessage("Rename file to:");

        // Set an EditText view to get user input
        final EditText input = new EditText(textView.getContext());
        input.setText(theFile);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Do something with value!
                File newFileName=new File(filePath + "/" + value);
                if(file.renameTo(newFileName))
                {
                    Log.e("Rename", "File rename has been successful");
                }
                else Log.e("Rename", "File Rename has failed");
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void imgPlayPauseChanger(ImageView v)
    {
        currentPosition=Integer.parseInt(v.getTag().toString());
        String fileName=v.getTag(R.string.file_name).toString();

        String sLastFileName=filePath + "/" + fileName;

        if(lastView!=null && currentPosition!=lastPosition)
        {
            ImageView p=(ImageView)lastView;
            p.setImageResource(R.drawable.playtrans);
            bPlay=false;

            if(mPlayer!=null)
            {
                if(mPlayer.isPlaying())
                {
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer=null;
                }
            }
        }

        if(!bPlay)
        {
            v.setImageResource(R.drawable.pause2);
            bPlay=true;

            if(mPlayer==null)
            {
                mPlayer=new MediaPlayer();
                mPlayer.setLooping(true);
                try {
                    mPlayer.setDataSource(sLastFileName);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IllegalStateException e) { e.printStackTrace();    }
                catch (IOException ioe) {    ioe.printStackTrace();         }
                catch(Exception e)      {    e.printStackTrace();           }
            }
        }
        else
        {
            v.setImageResource(R.drawable.playtrans);
            bPlay=false;

            if(mPlayer!=null)
            {
                mPlayer.stop();
                mPlayer.release();
                mPlayer=null;
            }
        }
    }

    public void imgDeleteAction(ImageView v)  {
        final String theFile=v.getTag(R.string.file_name).toString();
        final File file=new File(filePath + "/" + theFile);

        final int fetchedPosition=Integer.parseInt(v.getTag().toString());
        final AdapterAffirmation ad=this;

        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

        alert.setMessage("Sure, you want to delete: " + theFile + "?");
        alert.setTitle("Deleting..");

        alert.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }});

        alert.setPositiveButton("Oh Yeah!!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(file.delete())
                {
                    data.remove(fetchedPosition);

                    Affirmation af=new Affirmation(theFile);

                    ad.remove(af);
                    ad.notifyDataSetChanged();

                    if(fetchedPosition==currentPosition && mPlayer!=null)
                    {
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer=null;
                        bPlay=false;
                    }
                }else
                {
                    Log.e("Deletion","Failed");
                }
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}
