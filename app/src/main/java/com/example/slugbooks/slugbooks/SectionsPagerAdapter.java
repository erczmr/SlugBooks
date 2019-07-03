package com.example.slugbooks.slugbooks;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 1;
    }


    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "INBOX";
            default:
                return null;
        }
    }
}
