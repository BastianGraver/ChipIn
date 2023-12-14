package com.example.payapp.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.payapp.Fragments.AddTokens;
import com.example.payapp.Fragments.StoreFragment;
import com.example.payapp.R;
import com.example.payapp.Fragments.ReceiptFragment;
import com.example.payapp.database.User;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    User user;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, User user) {
        super(fm);
        mContext = context;
        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new StoreFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user); // pass user to the fragment
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new AddTokens();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user", user);
                fragment.setArguments(bundle1);
                break;
            case 2:
                fragment = new ReceiptFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("user", user);
                fragment.setArguments(bundle2);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}
