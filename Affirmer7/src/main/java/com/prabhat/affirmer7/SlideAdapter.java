package com.prabhat.affirmer7;

/**
 * Created by prabhat on 4/11/13.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SlideAdapter extends FragmentPagerAdapter
{
    public SlideAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int i) {
        //Fragment fragment
        Fragment fragment;

        if(i==0)
        {
            fragment=new FragmentAffirmations();
            Bundle args=new Bundle();
            args.putInt(FragmentAffirmations.ARG_OBJECT,i+1);
            fragment.setArguments(args);
            return fragment;
        }
        else if(i==1)
        {
            fragment=new FragmentRecorder();
            Bundle args=new Bundle();
            args.putInt(FragmentRecorder.ARG_OBJECT,i+1);
            fragment.setArguments(args);
            return fragment;
        }
        else if(i==2)
        {
            fragment=new FragmentAbout();
            Bundle args=new Bundle();
            args.putInt(FragmentAbout.ARG_OBJECT,i+1);
            fragment.setArguments(args);
            return fragment;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position==0) return "Affirmations";
            else if(position==1) return "Recorder";
                else if(position==2) return "About";
                    else if(position==3) return "NewList";

        return "No Title";
        //return super.getPageTitle(position);
    }
}
