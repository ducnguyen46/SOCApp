package com.ducnguyen46.soc.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ducnguyen46.soc.view.cart.CartFragment;
import com.ducnguyen46.soc.view.home.HomeFragment;
import com.ducnguyen46.soc.view.userpage.PersonalFragment;

public class BottomNavigationAdapter extends FragmentStatePagerAdapter {
    public BottomNavigationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new HomeFragment();
            case 1: return new CartFragment();
            case 2: return new PersonalFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
