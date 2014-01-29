package com.prabhat.affirmer7;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by prabhat on 17/10/13.
 */
public class FragmentAbout extends Fragment
{
    public static final String ARG_OBJECT = "object";
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_about,container,false);
        return(view);
    }
}
