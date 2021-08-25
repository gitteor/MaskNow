package com.booknbooks.maskinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.booknbooks.maskinfo.ui.home.Home;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class PopupActivity extends AppCompatActivity implements OnMapReadyCallback {


    GoogleMap map;

    TextView description;

    LatLng currentLatLng;

    Home item;


    TextView title;
    TextView address;
    TextView remain;
    TextView stockat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_popup);

        Intent intent = getIntent();

        item = (Home)intent.getSerializableExtra("class");



        currentLatLng = new LatLng(item.getLatitude(), item.getLongitude());

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment)fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
        fragment.getMapAsync(this);


        title = (TextView)findViewById(R.id.popup_title);
        address = (TextView)findViewById(R.id.popup_address);
        remain = (TextView) findViewById(R.id.popup_remain);
        stockat = (TextView) findViewById(R.id.popup_stockat);
    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.map = googleMap;

        map.setInfoWindowAdapter(null);

        //  map.setOnMarkerClickListener(this);


        //     setLastKnownLocation((Context)this);


        String fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;

        if (ActivityCompat.checkSelfPermission((Context)this, fineLocationPermission)
                != PackageManager.PERMISSION_GRANTED){

            map.setMyLocationEnabled(false);

        }else {

            map.setMyLocationEnabled(true);
        }



        //    map.setOnMapClickListener(this);
        //    map.setOnCameraMoveListener(this);

        //    map.setOnCameraMoveCanceledListener(this);
        //    map.setOnCameraIdleListener(this);

        UiSettings setting = map.getUiSettings();
        setting.setMyLocationButtonEnabled(true);
        setting.setCompassEnabled(true);
        setting.setZoomControlsEnabled(true);
        setting.setMapToolbarEnabled(false);


        map.clear();

        movePosition(currentLatLng, 15);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(item.getName());
        markerOptions.draggable(false);

        Marker marker = map.addMarker(markerOptions);
        marker.showInfoWindow();


        title.setText(item.getName());
        address.setText(item.getaddress());
        remain.setText(item.getRemain_inKorean());
        stockat.setText(item.getstock_at());
    }


    /**
     * 구글맵의 카메라를 위도와 경도 그리고 줌레벨을 기반으로 이동한다.
     *
     * @param latlng    위도, 경도 객체
     * @param zoomLevel 줌레벨
     */
    private void movePosition(LatLng latlng, float zoomLevel) {
        CameraPosition cp = new CameraPosition.Builder().target((latlng)).zoom(zoomLevel).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }



    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }



}
