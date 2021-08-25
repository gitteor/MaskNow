package com.gitteor.masknow.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gitteor.masknow.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HomeFragment extends Fragment {

    private RequestQueue mQueue;

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    Adapter adapter;

    LatLng currentLatLng ;

    ArrayList<Home> pharms = new ArrayList<>();

    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setLastKnownLocation((Context)getActivity());

        //mTextViewResult = view.findViewById(R.id.text_view_result);
        //Button buttonParse = view.findViewById(R.id.button_parcer);
        recyclerView = view.findViewById(R.id.dataList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        adapter = new Adapter(getActivity().getApplicationContext(), pharms, getFragmentManager());
        recyclerView.setAdapter(adapter);



        mQueue = Volley.newRequestQueue((Context) getActivity());

        jsonParce(currentLatLng.latitude, currentLatLng.longitude, 2000);



        textView = (TextView) view.findViewById(R.id.dayinfo);
        Calendar cal = Calendar.getInstance();
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);

        if (nWeek == 1) {
            textView.setText("일요일은 주중 구매 못하신 분 구매 가능");
        } else if (nWeek == 2) {
            textView.setText("월요일은 ○1년생 ○6년생 구매 가능합니다.");
        } else if (nWeek == 3) {
            textView.setText("화요일은 ○2년생 ○7년생 구매 가능합니다.");
        } else if (nWeek == 4) {
            textView.setText("수요일은 ○3년생 ○8년생 구매 가능합니다.");
        } else if (nWeek == 5) {
            textView.setText("목요일은 ○4년생 ○9년생 구매 가능합니다.");
        } else if (nWeek == 6) {
            textView.setText("금요일은 ○5년생 ○0년생 구매 가능합니다.");
        } else if (nWeek == 7) {
            textView.setText("토요일은 주중 구매 못하신 분 구매 가능");
        }



    }

    // private void jsonParce() {

    private void jsonParce(Double lat, Double lng, int m) {
        // final String url = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByGeo/json?lat=35.177641&lng=129.175059&m=1000";

        final String latitude = String.valueOf(lat);
        final String longitude = String.valueOf(lng);
        final String meter = String.valueOf(m);

        String url = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByGeo/json?lat=" + latitude

                + "&lng=" + longitude + "&m=" + meter;

        //String url = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByGeo/json?lat=35.322206&lng=129.183183&m=400";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("stores");

                            ArrayList<Home> listPlenty = new ArrayList<>();
                            ArrayList<Home> listSome = new ArrayList<>();
                            ArrayList<Home> listFew = new ArrayList<>();
                            ArrayList<Home> listEmpty = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject stores = jsonArray.getJSONObject(i);

                                Home pharm = new Home();
                                try {
                                    pharm.setName(stores.getString("name"));
                                } catch (Exception e) {
                                    pharm.setName("null");
                                }
                                try {
                                    pharm.setaddress(stores.getString("addr"));
                                } catch (Exception e) {
                                    pharm.setaddress("null");
                                }
                                try {
                                    pharm.setremain(stores.getString("remain_stat"));
                                } catch (Exception e) {
                                    pharm.setremain("null");
                                }
                                // type 추가
                                try {
                                    pharm.setType(stores.getString("type"));
                                } catch (Exception e) {
                                    pharm.setType("null");
                                }
                                try{
                                    pharm.setstock(stores.getString("stock_at"));
                                }catch (Exception e){

                                    pharm.setstock("null");
                                }

                                pharm.setLatitude(stores.getDouble("lat"));
                                pharm.setLongitude(stores.getDouble("lng"));

                                if (pharm.getremain().equals("plenty")) {
                                    listPlenty.add(pharm);
                                } else if (pharm.getremain().equals("some")) {
                                    listSome.add(pharm);
                                } else if (pharm.getremain().equals("few")) {
                                    listFew.add(pharm);
                                } else {
                                    listEmpty.add(pharm);
                                }

                            }

                            if(listPlenty!=null){
                                pharms.addAll(listPlenty);
                            }
                            if(listSome!=null) {
                                pharms.addAll(listSome);
                            }
                            if(listFew!=null) {
                                pharms.addAll(listFew);
                            }
                            pharms.addAll(listEmpty);

                            adapter.setItemList(pharms);


                            //mTextViewResult.append(name + ", " + address + "\n");

                        } catch (JSONException e) {
                            Toast.makeText((Context) getActivity(), "에러", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText((Context) getActivity(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();

            }
        });

        mQueue.add(request);
    }



    /**
     * 사용자의 현재 위도, 경도를 반환한다.
     * 실제로는 최근 측정된 위치 정보이다.
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
            currentLatLng =  new LatLng(location.getLatitude(), location.getLongitude());
            // currentLatLng.latitude = location.getLatitude();
            // currentLatLng.longitude = location.getLongitude();
        } else {
            //서울 설정
            // GeoItem.knownLatitude = 37.566229;
            // GeoItem.knownLongitude = 126.977689;
            Toast.makeText((Context) getActivity(), "위치정보를 확인할 수 없습니다", Toast.LENGTH_SHORT).show();
            currentLatLng = new LatLng(37.566229, 126.978289);
        }
    }








}