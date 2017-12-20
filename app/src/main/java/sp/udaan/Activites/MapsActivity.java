package sp.udaan.Activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import sp.udaan.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker marker1,marker2,marker3,marker4;
    private Marker prev_marker;
    static int tracker=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Added restrictions on zoom
        mMap.setMaxZoomPreference((float)14);
        mMap.setMinZoomPreference((float)10.5);

        mMap.setOnMarkerClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        LatLng location1 = new LatLng(19.229646, 72.843572);
        LatLng location2=new LatLng(19.1219362,72.8432649);
        LatLng location3=new LatLng(19.003302, 72.840541);
        LatLng location4=new LatLng(19.0556692,72.8158758);
        marker1=mMap.addMarker(new MarkerOptions().position(location1).title("Borivali").snippet("Fun Events"));
        marker1.showInfoWindow();
        marker2=mMap.addMarker(new MarkerOptions().position(location2).title("SPIT").snippet("Performing Arts"));
        marker2.showInfoWindow();
        marker3=mMap.addMarker(new MarkerOptions().position(location3).title("Worli").snippet("Literary Arts"));
        marker3.showInfoWindow();
        marker4=mMap.addMarker(new MarkerOptions().position(location4).title("Bandra").snippet("Featured"));
        marker4.showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(19.1112823,72.892324)).zoom((float) 10.5).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (tracker==0 || !prev_marker.equals(marker)) {
            tracker++;
            if (marker.equals(marker1)) {
                prev_marker=marker1;
            }   else if (marker.equals(marker2)) {
                prev_marker=marker2;
            }   else if (marker.equals(marker3)) {
                prev_marker=marker3;
            }   else if (marker.equals(marker4)){
                prev_marker=marker4;
            }
        }else{
            Intent i=new Intent(MapsActivity.this,MainActivity.class);
            tracker=0;
            if (marker.equals(marker1)) {
                prev_marker=marker1;
                i.putExtra("EventCategory","Fun Events");
            }   else if (marker.equals(marker2)) {
                prev_marker=marker2;
                i.putExtra("EventCategory","Performing Arts");
            }   else if (marker.equals(marker3)) {
                i.putExtra("EventCategory","Literary Arts");
            }   else if (marker.equals(marker4)) {
                i.putExtra("EventCategory","Featured");
            }
            startActivity(i);
        }
        return false;
    }
}
