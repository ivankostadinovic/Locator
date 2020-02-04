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
        for (User user : LocatorData.getInstance().friends) {
            friendsMap.put(user.getId(), user);
        }
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
            .icon(BitmapDescriptorFactory.fromAsset(updatedFriend.getImage()));
    }
}
