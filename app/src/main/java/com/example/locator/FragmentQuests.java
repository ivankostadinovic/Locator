package com.example.locator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class FragmentQuests extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FeedsListFragment feedsListFragment;
    private FragmentActiveList fragmentActiveList;
    private FragmentFinishedList fragmentFinishedList;


    private User user;

    public FragmentQuests() {
    }

    public static FragmentQuests newInstance(User user) {
        FragmentQuests fragment = new FragmentQuests();
        Bundle args = new Bundle();
        args.putSerializable("User", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("User");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.container_view_pager);
        tabLayout = view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_quests, container, false);

        return view;
    }


    private void setupViewPager(ViewPager viewPager) {

        feedsListFragment = new FeedsListFragment();
        fragmentActiveList = new FragmentActiveList();
        fragmentFinishedList = new FragmentFinishedList();
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager(), SectionsPageAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(fragmentActiveList, "Active");
        adapter.addFragment(feedsListFragment, "Feed");
        adapter.addFragment(fragmentFinishedList, "Finished");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    fragmentActiveList.updateAdapter();
                }else if(position == 1){
                    feedsListFragment.updateAdapter();
                }else{
                    fragmentFinishedList.updateAdapter();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
