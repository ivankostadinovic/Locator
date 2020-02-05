package com.example.locator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

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


import java.util.HashMap;
import java.util.Map;

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
            public void userLatitudeChagned(double latitude) {

            }

            @Override
            public void userLongitudeChanged(double longitude) {

            }

            @Override
            public void addedQuestListener(Quest quest) {
                addActiveQuest(quest);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_map, container, false);


        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(mMap -> {
                this.mMap = mMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.clear();
                mMap.setMyLocationEnabled(true);
                mMap.setOnMarkerClickListener(this);
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                for (User user : LocatorData.getInstance().friends) {
                    Marker marker = mMap.addMarker(generateMarker(user));
                    friendsMap.put(marker.getId(), user);
                    friendsMarkerMap.put(user.getId(), marker);
                }

                for (Quest quest : LocatorData.getInstance().activeQuests) {
                    Marker marker = mMap.addMarker(generateMarker(quest));
                    activeQuestMap.put(marker.getId(), quest);
                    friendsMarkerMap.put(quest.getId(), marker);
                }

                for (Quest quest : LocatorData.getInstance().feedQuests) {
                    Marker marker = mMap.addMarker(generateMarker(quest));
                    feedQuestMap.put(marker.getId(), quest);
                    friendsMarkerMap.put(quest.getId(), marker);
                }
                LocatorData.getInstance().observeFriendLocations(this);

                fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            googlePlex = CameraPosition.builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(15)
                                .bearing(0)
                                .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                        } else {
                            googlePlex = CameraPosition.builder()
                                .target(nisLatLng)
                                .zoom(15)
                                .bearing(0)
                                .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                        }
                    });
            });
        }
        return view;
    }

    public void updateAddFriend(User updatedFriend) {
        Marker marker = friendsMarkerMap.get(updatedFriend.getId());
        if (marker != null) {
            friendsMap.remove(marker.getId());
            marker.remove();
        }
        marker = mMap.addMarker(generateMarker(updatedFriend));
        friendsMap.put(marker.getId(), updatedFriend);
        friendsMarkerMap.put(updatedFriend.getId(), marker);
    }

    public void addFeedQuest(Quest quest) {
        Marker marker = mMap.addMarker(generateMarker(quest));
        feedQuestMap.put(marker.getId(), quest);
        feedQuestsMarkerMap.put(quest.getId(), marker);
    }

    public void addActiveQuest(Quest quest) {
        Marker marker = feedQuestsMarkerMap.get(quest.getId());
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(generateMarker(quest));
        activeQuestMap.put(marker.getId(), quest);
        activeQuestsMarkerMap.put(quest.getId(), marker);
    }

    private MarkerOptions generateMarker(User updatedFriend) {
        LatLng latLng = new LatLng(updatedFriend.getLatitude(), updatedFriend.getLongitude());
        return new MarkerOptions()
            .position(latLng)
            .title(updatedFriend.getName())
            .icon(BitmapDescriptorFactory.fromBitmap(Tools.StringToBitMap(image)));
    }

    private MarkerOptions generateMarker(Quest quest) {
        LatLng latLng = new LatLng(quest.getLatitude(), quest.getLongitude());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.quest);
        return new MarkerOptions()
            .position(latLng)
            .title(quest.getName())
            .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }


    private String image = "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCABkAGQDASIAAhEBAxEB/8QAFwABAQEBAAAAAAAAAAAAAAAABAYACP/EACEQAAMAAgIDAQEBAQAAAAAAAAADBGFiAiEBMVEyUhEi/8QAFgEBAQEAAAAAAAAAAAAAAAAABwYJ/8QAHBEAAgMBAQEBAAAAAAAAAAAAAAQFITEBAiIR/9oADAMBAAIRAxEAPwDn9kWoRkOpVth1Csh1B6Kaw0hkndslGw6hWQ6lWyHUIyHUSYprA/kndslWRahWRalUyHUKyHUSoprA/kndJVkOoRkWpVth1Csh1EmKawP5J3bJRkOoVkWpVsh1CMh8/wAiTFNYH8k7tkqyLUK2HUqmQ6hWw6iVFNYH8k7tkt5h7/Jig5Q9/kxaeGvnhJ+nbOmWQ4CshwVTIcBWQ4MN4prDT6Sd0lWQ4CshwVTIdQrIcCVFNYH8k7pKshwEZDgq2Q4Csh1EmKbwP5J3bJRkOArYcFUyHAVkOolRTWB/JO6SrIcBWQ4KpkOArIdRJimsD+Sd0lGQ4CshwVbIdQrIcCVFNYH8k7pLcoe/Rig5Q9/kxZ+GvnhJ+nbOmWQ6hGQ6lWyLUKyL3/yYcRTWGn0k7pKMh1Csh1KtkWoRkWolRTWB/JO7ZKsh1Csh1KpkOoVkOBJimsD+Sd2yVZDqEZDqVbIdQrItRJimsD+Sd2yUZD4/kKyHUq2xahGQ6iVFNYH8k7pKsh1Csh1KpkWoVkOokxTWB/JO6S3mHv8AJig5Rd+jFn4a+eEn6dvTplkOArIcFUyHAVkODDmKaw0+kndslWQ4CshwVTIcBWQ4EmKawP5J3bJVkOArIcFUyHAVkOBJimsD+Sd2yUbDgKyHBVMhwFbDgSoprA/kndslWQ4CshwVTYcBWQ4EmKawP5J3bJVkOArIcFUyHARkOBKimsD+Sd2yX5Q9+jFByh79GLPw388JL07enTLIcBWQ4KpkPj+QrIdTDiKaw0/kndslGw4CshwVTIdQrIdRJimsD+Sd0lWQ4Csh1KpkOoVkOolRTWB/JO6SrIdQrIcFUyHUKyHUSoprA/kndslGQ4CshwVTIdQrIdRJimsD+Sd2yVZDqFZDgqmQ6hWQ6iTFNYH8k7tktyh8f76MUHKHv0YtPDXzwk/TtnTLIdQrIcFWyHARkODDeKaw0+kndJVkOArIcFUyHAVsOBKimsD+Sd0lWQ4CMhwVbIcBWQ4EmKawP5J3SUZDgKyHUqmQ4CshwJUU1gfyTukqyHAVkPvoqmw4CshwJMU1gfyTukqyHARsOCrZDgKyHAlRTeB/JO6S3mHv0YoOUPfoxZ+G/nhJ+nbOlGJ4fAjEr+GMYixXcNTZLvbCtTw+BWJ4fDGEmK72g/ku9sK1PD4FYnh8MYSYrvaD+S72wrE8PgRqeHwxhKiu9oP5LvbCsTw+BWp4fDGEmK72iAku9sK1XD4FYlfwxhKiu9oPpLvbDeU8P99GMYs/He/nCT9d7+9P/9k=";

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
            openTakeQuestDialog(quest, marker);
        }
        return false;
    }

    private void openTakeQuestDialog(Quest quest, Marker marker) {
        new MaterialAlertDialogBuilder(getActivity())
            .setCancelable(false)
            .setTitle("Take quest")
            .setMessage("Do you want to take the quest?")
            .setPositiveButton("Take", (dialog, which) -> {
                LocatorData.getInstance().takeQuest(quest);
                LocatorData.getInstance().feedQuests.remove(quest);
                feedQuestMap.remove(marker.getId());
                feedQuestsMarkerMap.remove(quest.getId());
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
