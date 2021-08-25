package com.booknbooks.maskinfo.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.booknbooks.maskinfo.MainPage;
import com.booknbooks.maskinfo.R;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.booknbooks.maskinfo.InfoItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener,
        MainPage.onKeyBackPressedListener, GoogleMap.OnCameraIdleListener {


    private MapViewModel dashboardViewModel;

    GoogleMap map;

    Toast zoomGuideToast;

    TextView description;

    String address;

    private HashMap<Marker, InfoItem> markerMap = new HashMap<>();

    private RequestQueue mQueue;

    LatLng currentLatLng;

    int currentZoomLevel = 18;

    //ArrayList<InfoItem> infoList = new ArrayList<>();


    public static MapFragment newInstance(double lat, double lng, String address) {

        Bundle bundle = new Bundle();
        bundle.putDouble("LAT", lat);
        bundle.putDouble("LNG", lng);
        bundle.putString("Address", address);
        MapFragment f = new MapFragment();
        f.setArguments(bundle);

        return f;
    }


    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        if (getArguments() != null) {

            // queryItem = Parcels.unwrap( getArguments().getParcelable("QueryItem"));
            double argLat = getArguments().getDouble("LAT");
            double argLng = getArguments().getDouble("LNG");

            address = getArguments().getString("Address");
            currentLatLng = new LatLng(argLat, argLng);

            //MainPage.toggle = true;

        } else {
            setLastKnownLocation((Context) getActivity());
        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // final TextView textView = root.findViewById(R.id.js_string);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
        fragment.getMapAsync(this);

        mQueue = Volley.newRequestQueue((Context) getActivity());


        description = (TextView) view.findViewById(R.id.description);


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.map = googleMap;

        map.setInfoWindowAdapter(null);

        map.setOnMarkerClickListener(this);



        String fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;

        if (ActivityCompat.checkSelfPermission((Context) getActivity(), fineLocationPermission)
                != PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(false);
        } else {
            map.setMyLocationEnabled(true);
        }


        map.setOnMapClickListener(this);
        map.setOnCameraMoveListener(this);

        map.setOnCameraMoveCanceledListener(this);
        map.setOnCameraIdleListener(this);

        UiSettings setting = map.getUiSettings();
        setting.setMyLocationButtonEnabled(true);
        setting.setCompassEnabled(true);
        setting.setZoomControlsEnabled(true);
        setting.setMapToolbarEnabled(false);


        map.clear();

        movePosition(currentLatLng, 16);

        showList();



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


    private void jsonParce(Double lat, Double lng, int m) {
        //  final String url = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByGeo/json?lat=35.16669&lng=129.13384&m=1000";

        final String latitude = String.valueOf(lat);
        final String longitude = String.valueOf(lng);
        final String meter = String.valueOf(m);

        String url = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByGeo/json?lat=" + latitude

                + "&lng=" + longitude + "&m=" + meter;

        // final ArrayList<InfoItem> list = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("stores");

                            ArrayList<InfoItem> list = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject stores = jsonArray.getJSONObject(i);

                                InfoItem item = new InfoItem();

                                try {
                                    item.name = stores.getString("name");
                                } catch (Exception e) {
                                    item.name = "null";
                                }

                                try {
                                    item.addr = stores.getString("addr");
                                } catch (Exception e) {
                                    item.addr = "null";
                                }

                                try {
                                    item.type = stores.getString("type");
                                } catch (Exception e) {
                                    item.type = "null";
                                }

                                item.lat = stores.getDouble("lat");
                                item.lng = stores.getDouble("lng");


                                try {
                                    item.stock_at = stores.getString("stock_at");
                                } catch (Exception e) {
                                    item.stock_at = "null";
                                }

                                try {
                                    item.remain_stat = stores.getString("remain_stat");
                                } catch (Exception e) {
                                    item.remain_stat = "null";
                                }

                                list.add(item);
                            }

                            setMap(list);

                        } catch (JSONException e) {
                            //Toast.makeText((Context) getActivity(), "지도를 살짝 옆으로 옮겨보세요.", Toast.LENGTH_SHORT).show();
                            // handler1.sendEmptyMessageDelayed(0, 1000);
                            e.printStackTrace();

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                Toast.makeText((Context) getActivity(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();

            }
        });

        mQueue.add(request);

    }


    private void setMap(ArrayList<InfoItem> list) {
        if (map != null && list != null) {
            map.clear();
            addMarker(list);
        }

    }


    private void addMarker(ArrayList<InfoItem> list) {
        //  MyLog.d(TAG, "addMarker list.size() " + list.size());

        if (list == null || list.size() == 0) return;

        int num;

        if (list.size() > 500) {
            num = 500;
        } else {
            num = list.size();
        }


        //  for (InfoItem item : list) {
        for (int i = 0; i < num; i++) {
            //   MyLog.d(TAG, "addMarker " + item);

            InfoItem item = new InfoItem();
            item = list.get(i);

            if (item.lat != 0 && item.lng != 0) {
                Marker marker = map.addMarker(getMarker(item));
                //  if (getMarker(item).getPosition()==currentLatLng) {
                marker.showInfoWindow();
                //  }
                markerMap.put(marker, item);
            }
        }
    }


    private MarkerOptions getMarker(InfoItem item) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(item.lat, item.lng));
        if (address != null) {
            if (address.equals(item.addr)) {
                marker.title(item.name);
            }
        }

        // final BitmapDrawable bimapdraw = new BitmapDrawable();

        if (item.remain_stat.equals("plenty")) {
            if (item.type.equals("01")) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));
            } else if (item.type.equals("02")) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker5));
            } else {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker9));
            }
        }

        else if (item.remain_stat.equals("some")) {
            if (item.type.equals("01")) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));
            } else if (item.type.equals("02")) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker6));
            } else {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker10));
            }
        }

        else if (item.remain_stat.equals("few")) {
            if (item.type.equals("01")) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3));
            } else if (item.type.equals("02")) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker7));
            } else {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker11));
            }
        }

        else if (item.remain_stat.equals("empty")) {
            if (item.type.equals("01")) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4));
            } else if (item.type.equals("02")) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker8));
            } else {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker12));
            }
        }
        else {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4));
        }


        // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4));
        //  marker.snippet(item.name);
        marker.draggable(false);


        return marker;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        InfoItem item = markerMap.get(marker);

        description.setText(item.toString());
        description.setVisibility(View.VISIBLE);
        ((MainPage) getActivity()).setmOnKeyBackPressedListener(this);

        return true;
    }


    @Override
    public void onMapClick(LatLng latLng) {

        description.setVisibility(View.INVISIBLE);

    }


    /**
     * 지도를 움직일 경우 강연 정보를 조회해서 화면에 표시할 수 있도록 한다.
     */
    @Override
    public void onCameraMove() {
        //  showList();
    }


    /**
     * 지도를 일정 레벨 이상 확대했을 경우, 해당 위치에 있는 강연 리스트를 서버에 요청한다.
     */
    private void showList() {
        currentZoomLevel = (int) map.getCameraPosition().zoom;
        currentLatLng = map.getCameraPosition().target;

        if (currentZoomLevel < 10) {

            map.clear();

            if (zoomGuideToast != null) {
                zoomGuideToast.cancel();
            }
            zoomGuideToast = Toast.makeText((Context) getActivity()
                    , "지도를 확대해야 위치 정보가 표시됩니다."
                    , Toast.LENGTH_SHORT);
            zoomGuideToast.show();

            return;
        }


        // infolist(35.158262,129.04684,1000);

        jsonParce(currentLatLng.latitude, currentLatLng.longitude, 2500);

    }


    /**
     * 사용자의 현재 위도, 경도를 반환한다.
     * 실제로는 최근 측정된 위치 정보이다.
     *
     * @param context 컨텍스트 객체
     */
    public void setLastKnownLocation(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        int result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if (location != null) {


            // new LatLng(35.158262, 129.04684);
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            // currentLatLng.latitude = location.getLatitude();
            // currentLatLng.longitude = location.getLongitude();
        } else {
            //서울 설정
            // GeoItem.knownLatitude = 37.566229;
            // GeoItem.knownLongitude = 126.977689;
            // currentLatLng = new LatLng(35.158262, 129.04684);
            Toast.makeText((Context) getActivity(), "위치정보를 확인할 수 없습니다", Toast.LENGTH_SHORT).show();
            currentLatLng = new LatLng(37.566229, 126.978289);
        }
    }


    @Override
    public void onBackKey() {

        description.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //((MainPage)context).setmOnKeyBackPressedListener(this);
    }


    @Override
    public void onCameraMoveCanceled() {
        // showList();
    }

    @Override
    public void onCameraIdle() {
        showList();
    }



}

