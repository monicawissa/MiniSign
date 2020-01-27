package com.example.minisign.ui.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class tabsAdapter extends FragmentPagerAdapter {
    public tabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                chatsFragment chatsFragment=new chatsFragment();
                return chatsFragment;

            case 1:
                groupsFragment groupsFragment=new groupsFragment();
                return groupsFragment;
            case 2:
                contactsFragment contactsFragment=new contactsFragment();
                return contactsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "chats";

            case 1:
                return "groups";
            case 2:
                return "contacts";
            default:
                return null;
        }
    }
}
