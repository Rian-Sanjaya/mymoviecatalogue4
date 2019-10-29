package com.lonecode.mymoviecatalogue4.ui.favorite;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FavoritePageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public FavoritePageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FavoriteMovieFragment();
            case 1: return new FavoriteTvShowFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
