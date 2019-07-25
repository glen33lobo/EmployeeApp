package com.example.android.employeeapp.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.employeeapp.Emp_info;
import com.example.android.employeeapp.Emp_upload;
import com.example.android.employeeapp.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    Bundle bundle;
    String id;

    public SectionsPagerAdapter(Context context, FragmentManager fm,String id) {
        super(fm);
        mContext = context;
        bundle=new Bundle();
        this.id=id;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                Emp_info e1=new Emp_info();
                bundle.putString("id", String.valueOf(id));
                e1.setArguments(bundle);
                return e1;
            case 1:
                Emp_upload e2=new Emp_upload();
                bundle.putString("id", String.valueOf(id));
                e2.setArguments(bundle);
                return e2;
                default:return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}