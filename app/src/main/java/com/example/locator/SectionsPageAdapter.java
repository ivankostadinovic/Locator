package com.example.locator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList=new ArrayList<>();

    private List<String> tittleList=new ArrayList<>();

    SectionsPageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public void addFragment(Fragment fragment,String title)
    {
        fragmentList.add(fragment);
        tittleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tittleList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
      return   fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
