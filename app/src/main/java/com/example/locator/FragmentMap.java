package com.example.locator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentMap extends Fragment implements OnMapReadyCallback {
    private List<Quest> questList = new ArrayList<>();

    private Map<String, Marker> markersMap = new HashMap<>();
    private Map<String, User> friendsMap = new HashMap<>();
    private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment supportMapFragment;
    private CameraPosition googlePlex;
    private LatLng nisLatLng;
    private GoogleMap mMap;

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
                mMap.clear(); //clear old markersMap
                mMap.setMyLocationEnabled(true);
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                for (User user : LocatorData.getInstance().friends) {
                    friendsMap.put(user.getId(), user);
                    markersMap.put(user.getId(), mMap.addMarker(generateMarker(user)));
                }
                LocatorData.getInstance().observeFriendLocations(this);

                fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            googlePlex = CameraPosition.builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(10)
                                .bearing(0)
                                .tilt(45)
                                .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                        } else {
                            googlePlex = CameraPosition.builder()
                                .target(nisLatLng)
                                .zoom(10)
                                .bearing(0)
                                .tilt(45)
                                .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                        }
                    });
            });
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    public void updateFriendInfo(User updatedFriend) {
        if (mMap == null) {
            return;
        }
        User friend = friendsMap.get(updatedFriend.getId());
        if (friend != null) {
            Marker marker = markersMap.get(String.valueOf(friend.getLatitude() + friend.getLongitude()));
            if (marker != null) {
                marker.remove();
            }
            friendsMap.put(friend.getId(), updatedFriend);
            markersMap.put(String.valueOf(updatedFriend.getLatitude() + updatedFriend.getLongitude()), mMap.addMarker(generateMarker(updatedFriend)));
        }
    }

    private MarkerOptions generateMarker(User updatedFriend) {
        LatLng latLng = new LatLng(updatedFriend.getLatitude(), updatedFriend.getLongitude());
        return new MarkerOptions()
            .position(latLng)
            .title(updatedFriend.getName())
            .icon(BitmapDescriptorFactory.fromBitmap(Tools.StringToBitMap(image)));
    }

    private String image = "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCABkAGQDASIAAhEBAxEB/8QAFwABAQEBAAAAAAAAAAAAAAAABAYACP/EACEQAAMAAgIDAQEBAQAAAAAAAAADBGFiAiEBMVEyUhEi/8QAFgEBAQEAAAAAAAAAAAAAAAAABwYJ/8QAHBEAAgMBAQEBAAAAAAAAAAAAAAQFITEBAiIR/9oADAMBAAIRAxEAPwDn9kWoRkOpVth1Csh1B6Kaw0hkndslGw6hWQ6lWyHUIyHUSYprA/kndslWRahWRalUyHUKyHUSoprA/kndJVkOoRkWpVth1Csh1EmKawP5J3bJRkOoVkWpVsh1CMh8/wAiTFNYH8k7tkqyLUK2HUqmQ6hWw6iVFNYH8k7tkt5h7/Jig5Q9/kxaeGvnhJ+nbOmWQ4CshwVTIcBWQ4MN4prDT6Sd0lWQ4CshwVTIdQrIcCVFNYH8k7pKshwEZDgq2Q4Csh1EmKbwP5J3bJRkOArYcFUyHAVkOolRTWB/JO6SrIcBWQ4KpkOArIdRJimsD+Sd0lGQ4CshwVbIdQrIcCVFNYH8k7pLcoe/Rig5Q9/kxZ+GvnhJ+nbOmWQ6hGQ6lWyLUKyL3/yYcRTWGn0k7pKMh1Csh1KtkWoRkWolRTWB/JO7ZKsh1Csh1KpkOoVkOBJimsD+Sd2yVZDqEZDqVbIdQrItRJimsD+Sd2yUZD4/kKyHUq2xahGQ6iVFNYH8k7pKsh1Csh1KpkWoVkOokxTWB/JO6S3mHv8AJig5Rd+jFn4a+eEn6dvTplkOArIcFUyHAVkODDmKaw0+kndslWQ4CshwVTIcBWQ4EmKawP5J3bJVkOArIcFUyHAVkOBJimsD+Sd2yUbDgKyHBVMhwFbDgSoprA/kndslWQ4CshwVTYcBWQ4EmKawP5J3bJVkOArIcFUyHARkOBKimsD+Sd2yX5Q9+jFByh79GLPw388JL07enTLIcBWQ4KpkPj+QrIdTDiKaw0/kndslGw4CshwVTIdQrIdRJimsD+Sd0lWQ4Csh1KpkOoVkOolRTWB/JO6SrIdQrIcFUyHUKyHUSoprA/kndslGQ4CshwVTIdQrIdRJimsD+Sd2yVZDqFZDgqmQ6hWQ6iTFNYH8k7tktyh8f76MUHKHv0YtPDXzwk/TtnTLIdQrIcFWyHARkODDeKaw0+kndJVkOArIcFUyHAVsOBKimsD+Sd0lWQ4CMhwVbIcBWQ4EmKawP5J3SUZDgKyHUqmQ4CshwJUU1gfyTukqyHAVkPvoqmw4CshwJMU1gfyTukqyHARsOCrZDgKyHAlRTeB/JO6S3mHv0YoOUPfoxZ+G/nhJ+nbOlGJ4fAjEr+GMYixXcNTZLvbCtTw+BWJ4fDGEmK72g/ku9sK1PD4FYnh8MYSYrvaD+S72wrE8PgRqeHwxhKiu9oP5LvbCsTw+BWp4fDGEmK72iAku9sK1XD4FYlfwxhKiu9oPpLvbDeU8P99GMYs/He/nCT9d7+9P/9k=";
}
