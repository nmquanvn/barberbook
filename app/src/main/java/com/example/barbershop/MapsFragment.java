package com.example.barbershop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.barbershop.entity.Barbershop;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    Button btnSearchLocation;
    Button btnCurrent;
    View mapView;
    AutocompleteSupportFragment txtSearchStart;
    LatLng current;
    private List<Polyline> polylines = null;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    AutocompleteSupportFragment autocompleteFragment;
    Marker curMarker = null;
    LatLng closestLocation = new LatLng(0, 0);
    float smallestDistance = -1;
    List<LatLng> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // get all view of activity
        mapView = mapFragment.getView();
        getAllView();
    }

    private void getAllBarberShops() {
        ArrayList<Barbershop> barbershops = Barbershop.loadfromDB();
        locations = new ArrayList<>();

        for (Barbershop barbershop: barbershops) {
            LatLng latLng = new LatLng(barbershop.latitude, barbershop.longitude);
            writeMarkerOnMap(latLng, barbershop.title);
            locations.add(latLng);
        }
    }

    private void findNearestOnMap() {
        for(LatLng location : locations){
            float[] distances = new float[] {1};
            Location.distanceBetween(current.latitude,
                    current.longitude,
                    location.latitude,
                    location.longitude, distances);
            if(smallestDistance == -1 || distances[0] < smallestDistance){
                closestLocation = location;
                smallestDistance = distances[0];
            }
        }
    }

    private void writeMarkerOnMap(LatLng latLng, String title) {
        mMap.addMarker(new MarkerOptions().title(title).position(latLng));
    }

    private void getAllView() {
        btnCurrent = mapView.findViewById(R.id.btnCurrent);
        btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCurrentLocation();
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.txtSearchStart);
        autocompleteFragment.setHint("Tìm vị trí");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                LatLng selected = place.getLatLng();

                if (selected != null) {
                    if (curMarker != null) {
                        curMarker.remove();
                    }
                    current = selected;
                    curMarker = mMap.addMarker(new MarkerOptions().title("Vị trí của bạn").icon(bitmapDescriptorFromVector(MapsFragment.this, R.drawable.ic_location_on)).position(selected));
                    moveCameraToCurrent();
                    findNearestOnMap();
                    FindRoutes(selected, closestLocation);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.e("Places Error", status.toString());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getAllBarberShops();
        loadCurrentLocation();
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void loadCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location update
            LocationRequest mLocationRequest = LocationRequest.create();
            //mLocationRequest.setInterval(60000);
            //mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    //getLastLocation();
                }
            };
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    //if(task. == false) Toast.makeText(MapsFragment.this, "Không thể tìm thấy vị trí của bạn, hãy thử lại hoặc nhập vị trí của bạn trên thanh tìm kiếm", Toast.LENGTH_SHORT).show();
                    getLastLocation();
                }
            });
        } else {
            Toast.makeText(MapsFragment.this, "Không có quyền truy cập vị trí", Toast.LENGTH_SHORT).show();
            Log.e("Current location Error", "not have permission");
        }
    }

    private void getLastLocation() {
        // get last location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MapsFragment.this, "Không có quyền truy cập vị trí", Toast.LENGTH_SHORT).show();
            Log.e("Current location Error", "not have permission");
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            current = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.icon(bitmapDescriptorFromVector(MapsFragment.this, R.drawable.ic_location_on));
                            markerOptions.position(current);
                            markerOptions.title("Bạn đang ở đây");
                            if (curMarker != null) curMarker.remove();
                            curMarker = mMap.addMarker(markerOptions);
                            moveCameraToCurrent();
                            findNearestOnMap();
                            FindRoutes(current, closestLocation);
                        } else {
                            Toast.makeText(MapsFragment.this, "Không thể tìm thấy vị trí của bạn, hãy thử lại hoặc nhập vị trí của bạn trên thanh tìm kiếm", Toast.LENGTH_LONG).show();
                            Log.e("Current location Error", "location is null");
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(MapsFragment.this, "Không thể kết nối với dịch vụ google", Toast.LENGTH_SHORT).show();
                        Log.e("Current location Error", e.toString());
                    }
        });
    }

    private void moveCameraToCurrent() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16.0F));
    }

    private void moveCameraToPath(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(current);
        builder.include(closestLocation);
        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            autocompleteFragment.setText(place.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    public void FindRoutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(this, "Không tìm thấy địa điểm", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(getString(R.string.google_maps_key))
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        System.out.println(e);
    }

    @Override
    public void onRoutingStart() {
        //Toast.makeText(this, "Đang tìm...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(current);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < routes.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(ContextCompat.getColor(this, R.color.black));
                polyOptions.width(7);
                polyOptions.addAll(routes.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            }
        }

        moveCameraToPath();
    }

    @Override
    public void onRoutingCancelled() {
    }
}