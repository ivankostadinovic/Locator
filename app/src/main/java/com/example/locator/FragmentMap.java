package com.example.locator;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;


public class FragmentMap extends Fragment implements OnMapReadyCallback {
    private List<Quest> questList;
    private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment supportMapFragment;
    private CameraPosition googlePlex;
    private LatLng nisLatLng;

    private User user;

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
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("User");
        }
        nisLatLng = new LatLng(43.326233, 21.906442);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_map, container, false);


        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment != null) {//use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.clear(); //clear old markers
                    mMap.setMyLocationEnabled(true);
                    final GoogleMap mmMap = mMap;
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        googlePlex = CameraPosition.builder()
                                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                                .zoom(10)
                                                .bearing(0)
                                                .tilt(45)
                                                .build();
                                        mmMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                                    } else {
                                        googlePlex = CameraPosition.builder()
                                                .target(nisLatLng)
                                                .zoom(10)
                                                .bearing(0)
                                                .tilt(45)
                                                .build();
                                        mmMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                                    }
                                }
                            });
                }
            });
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
}
