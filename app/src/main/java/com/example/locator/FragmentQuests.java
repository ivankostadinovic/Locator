package com.example.locator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class FragmentQuests extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentQuestList addedQuestFragment;
    private FragmentQuestList fragmentActiveList;
    private FragmentQuestList fragmentFinishedList;


    public FragmentQuests() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addedQuestFragment = new FragmentQuestList(Constants.QuestType.ADDED);
        fragmentActiveList = new FragmentQuestList(Constants.QuestType.ACTIVE);
        fragmentFinishedList = new FragmentQuestList(Constants.QuestType.FINISHED);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.container_view_pager);
        tabLayout = view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        LocatorData.getInstance().setQuestsChangedListener(() -> {
            switch (tabLayout.getSelectedTabPosition()){
                case 0:
                    fragmentActiveList.updateAdapter();
                case 1:
                    addedQuestFragment.updateAdapter();
                case 2:
                    fragmentFinishedList.updateAdapter();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_quests, container, false);
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager(), SectionsPageAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(fragmentActiveList, "Active");
        adapter.addFragment(addedQuestFragment, "Added");
        adapter.addFragment(fragmentFinishedList, "Finished");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fragmentActiveList.updateAdapter();
                } else if (position == 1) {
                    addedQuestFragment.updateAdapter();
                } else {
                    fragmentFinishedList.updateAdapter();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
