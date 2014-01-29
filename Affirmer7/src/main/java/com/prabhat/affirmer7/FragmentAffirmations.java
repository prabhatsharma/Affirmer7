package com.prabhat.affirmer7;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by prabhat on 4/11/13.
 */
public class FragmentAffirmations extends Fragment
{
    public static final String ARG_OBJECT = "object";
    OnFragmentInteractionListener mCallBack=null;

    ArrayList<String> FilesInFolder;
    ArrayList<Affirmation> affirmation_data;
    AdapterAffirmation ad;

    ListView lv=null;

    String path= "AudioRecorder";


    View view;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_affirmations,container,false);

        FilesInFolder = GetFiles(path);
        affirmation_data=new ArrayList<Affirmation>();

        for(int i=0;i<FilesInFolder.size();i++)
        {
            affirmation_data.add(new Affirmation(R.drawable.delete2trans,FilesInFolder.get(i).toString(),R.drawable.playtrans));
        }


        ad = new AdapterAffirmation(this.getActivity(), android.R.layout.simple_list_item_1,affirmation_data);

        lv = (ListView)view.findViewById(R.id.lvMain);
        lv.setAdapter(ad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        String filename=(String)lv.getItemAtPosition(position).toString();
                        mCallBack.onFragmentInteraction(filename);
            }
        });

        return(view);
    }

    public void reset()
    {
        ad.clear();
        affirmation_data.clear();

        for(int i=0;i<FilesInFolder.size();i++)
        {
            affirmation_data.add(new Affirmation(R.drawable.delete2trans,FilesInFolder.get(i).toString(),R.drawable.playtrans));
        }

        ad.notifyDataSetChanged();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.e("FragmentAffirmations.onPause()", "FragmentAffirmations is paused");

    }

    public ArrayList<String> GetFiles(String DirectoryPath)
    {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f= new File(Environment.getExternalStorageDirectory().toString() + "/" + DirectoryPath);

        boolean mkdirsResult=f.mkdirs();
        File[] files = f.listFiles();

        int mFileCount=0;

        if(files != null)
        {
            for (int i = 0; i < files.length; i++){
                {
                    mFileCount++;
                    MyFiles.add(files[i].getName());
                }
            }
        }
        else
        {
            MyFiles.add("Failed to get affirmation files :(");
            MyFiles.add("files is " + files);
            MyFiles.add("filecount: " + mFileCount);
            MyFiles.add("Requested dir is: " + DirectoryPath);
            MyFiles.add("Req absol path: " + f.getAbsolutePath());
            MyFiles.add("mkdirs result is: " + mkdirsResult);
        }
        return MyFiles;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {
            mCallBack = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener
    {
        public void onFragmentInteraction(String id);
    }
}