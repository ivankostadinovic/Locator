package com.example.locator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentMap extends Fragment implements GoogleMap.OnMarkerClickListener {
    private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment supportMapFragment;
    private CameraPosition googlePlex;
    private LatLng nisLatLng;
    private GoogleMap mMap;
    private Map<String, Quest> activeQuestMap = new HashMap<>();
    private Map<String, Quest> feedQuestMap = new HashMap<>();
    private Map<String, User> friendsMap = new HashMap<>();
    private Map<String, Marker> friendsMarkerMap = new HashMap<>();
    private Map<String, Marker> feedQuestsMarkerMap = new HashMap<>();
    private Map<String, Marker> activeQuestsMarkerMap = new HashMap<>();
    private double filterRadius = 0;
    private String filterType = Constants.QuestTypes.ALL;


    public FragmentMap() {
        // Required empty public constructor
    }

    public static FragmentMap newInstance(User user) {
        FragmentMap fragment = new FragmentMap();
        Bundle args = new Bundle();
        args.putSerializable("User", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nisLatLng = new LatLng(43.326233, 21.906442);
        LocatorData.getInstance().setListener(new UserActionListener() {
            @Override
            public void newFeedQuest(Quest quest) {
                addFeedQuest(quest);
            }


            @Override
            public void addedQuestListener(Quest quest) {
                addActiveQuest(quest);
            }

            @Override
            public void friendLoadedListener(User friend) {
                updateAddFriend(friend);
            }

            @Override
            public void updateQuest(Quest quest) {
                updateActiveQuest(quest);
            }

            @Override
            public void removeQuest(Quest quest) {
                removeQuestFromMap(quest);
            }


        });
    }

    private void removeQuestFromMap(Quest quest) {
        Marker marker = activeQuestsMarkerMap.get(quest.getId());
        if (marker != null) {
            activeQuestMap.remove(marker.getId());
            activeQuestsMarkerMap.remove(quest.getId());
            marker.remove();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_map, container, false);


        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(mMap -> {
                LocatorData.getInstance().feedQuestListener();
                LocatorData.getInstance().observeFriends();
                LocatorData.getInstance().observeActiveQuests();
                this.mMap = mMap;
                this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                this.mMap.clear();
                this.mMap.setMyLocationEnabled(true);
                this.mMap.setOnMarkerClickListener(this);
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                LocatorData.getInstance().observeFriendLocations(this);

                fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            googlePlex = CameraPosition.builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(15)
                                .bearing(0)
                                .build();
                            this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                        } else {
                            googlePlex = CameraPosition.builder()
                                .target(nisLatLng)
                                .zoom(15)
                                .bearing(0)
                                .build();
                            this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                        }
                    });
            });
        }

        return view;
    }

    public void openFilterPop() {
        Intent intent = new Intent(getActivity(), PopUpFilterQuest.class);
        intent.putExtra("type", filterType);
        intent.putExtra("radius", filterRadius);
        startActivityForResult(intent, 1111);
    }

    public void updateAddFriend(User updatedFriend) {
        if (updatedFriend.getProfilePicture() == null) {
            return;
        }
        Marker marker = friendsMarkerMap.get(updatedFriend.getId());
        if (marker != null) {
            friendsMap.remove(marker.getId());
            marker.remove();
        }
        marker = mMap.addMarker(generateMarker(updatedFriend));
        marker.showInfoWindow();
        friendsMap.put(marker.getId(), updatedFriend);
        friendsMarkerMap.put(updatedFriend.getId(), marker);
    }

    public void updateActiveQuest(Quest quest) {
        Marker marker = activeQuestsMarkerMap.get(quest.getId());
        activeQuestMap.put(marker.getId(), quest);
    }

    public void addFeedQuest(Quest quest) {
        Marker marker = mMap.addMarker(generateMarker(quest, false));
        marker.showInfoWindow();
        feedQuestMap.put(marker.getId(), quest);
        feedQuestsMarkerMap.put(quest.getId(), marker);
    }

    public void addActiveQuest(Quest quest) {
        Marker marker = feedQuestsMarkerMap.get(quest.getId());
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(generateMarker(quest, true));
        marker.showInfoWindow();
        activeQuestMap.put(marker.getId(), quest);
        activeQuestsMarkerMap.put(quest.getId(), marker);
    }

    private MarkerOptions generateMarker(User updatedFriend) {
        LatLng latLng = new LatLng(updatedFriend.getLatitude(), updatedFriend.getLongitude());
        return new MarkerOptions()
            .position(latLng)
            .title(updatedFriend.getName())
            .icon(BitmapDescriptorFactory.fromBitmap(Tools.StringToBitMap(updatedFriend.getProfilePicture())));
    }

    private MarkerOptions generateMarker(Quest quest, boolean isActive) {
        LatLng latLng = new LatLng(quest.getLatitude(), quest.getLongitude());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), isActive ? R.drawable.active : R.drawable.quest);
        return new MarkerOptions()
            .position(latLng)
            .title(quest.getName())
            .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Quest quest = feedQuestMap.get(marker.getId());
        if (quest == null) {
            quest = activeQuestMap.get(marker.getId());
            if (quest == null) {
                User friend = friendsMap.get(marker.getId());
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("friend_id", friend.getId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), ActivityQuestProgress.class);
                intent.putExtra("quest", quest);
                startActivity(intent);
            }
        } else {
            Location ql = new Location("");
            ql.setLatitude(quest.getLatitude());
            ql.setLongitude(quest.getLongitude());

            Location ul = new Location("");
            ul.setLatitude(LocatorData.getInstance().getUser().getLatitude());
            ul.setLongitude(LocatorData.getInstance().getUser().getLongitude());

            if (ql.distanceTo(ul) > 50) {
                Tools.showMsg(getActivity(), "too far away from quest");
            } else {
                openTakeQuestDialog(quest, marker);
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1111 && resultCode == RESULT_OK) {
            filterType = data.getStringExtra("type");
            filterRadius = data.getDoubleExtra("radius", 0);
            Tools.showMsg(getActivity(), filterType + " " + filterRadius);
            filterQuests();
        }

    }

    private void filterQuests() {
        mMap.clear();
        activeQuestsMarkerMap.clear();
        activeQuestMap.clear();
        feedQuestsMarkerMap.clear();
        feedQuestMap.clear();
        friendsMap.clear();
        friendsMarkerMap.clear();
        Location current = new Location("");
        current.setLongitude(LocatorData.getInstance().getUser().getLongitude());
        current.setLatitude(LocatorData.getInstance().getUser().getLatitude());

        for (User friend : LocatorData.getInstance().friends) {
            updateAddFriend(friend);
        }
        for (Quest quest : LocatorData.getInstance().activeQuests) {
            Location questLocation = new Location("");
            questLocation.setLongitude(quest.getLongitude());
            questLocation.setLatitude(quest.getLatitude());
            if (questLocation.distanceTo(current) < filterRadius && (filterType.equals(Constants.QuestTypes.ALL) || quest.getType().equalsIgnoreCase(filterType))) {
                addActiveQuest(quest);
            }
        }

        for (Quest quest : LocatorData.getInstance().feedQuests) {
            Location questLocation = new Location("");
            questLocation.setLongitude(quest.getLongitude());
            questLocation.setLatitude(quest.getLatitude());
            if (questLocation.distanceTo(current) < filterRadius && (filterType.equals(Constants.QuestTypes.ALL) || quest.getType().equalsIgnoreCase(filterType))) {
                addFeedQuest(quest);
            }
        }
    }

    private void openTakeQuestDialog(Quest quest, Marker marker) {
        new MaterialAlertDialogBuilder(getActivity())
            .setCancelable(false)
            .setTitle("Take quest")
            .setMessage("Do you want to take the quest?")
            .setPositiveButton("Take", (dialog, which) -> {
                LocatorData.getInstance().takeQuest(quest);
                feedQuestMap.remove(marker.getId());
                feedQuestsMarkerMap.remove(quest.getId());
                marker.remove();
                activeQuestMap.put(marker.getId(), quest);
                activeQuestsMarkerMap.put(quest.getId(), marker);
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), ActivityQuestProgress.class);
                intent.putExtra("quest", quest);
                startActivity(intent);
            })
            .setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            }).show();
    }
}
