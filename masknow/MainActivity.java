package com.gitteor.masknow;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class MainActivity extends Activity {

    private final String TAG = getClass().getSimpleName();
    private static int REQUEST_ACCESS_FINE_LOCATION = 1000;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2500);

//        setContentView(R.layout.activity_main);
////        BottomNavigationView navView = findViewById(R.id.nav_view);
////        // Passing each menu ID as a set of Ids because each
////        // menu should be considered as top level destinations.
////        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
////                .build();
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
////        NavigationUI.setupWithNavController(navView, navController);
    }

    private class splashhandler implements Runnable {
        public void run() {

            // GPS 권한
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    // 안내문
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);

                    alert_confirm.setMessage("주변 마스크 정보를 제공해드리기 위해 위치정보 권한이 필요합니다.");
                    // 확인 클릭 리스너
                    alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                startActivity(new Intent(getApplication(), MainPage.class));
                                finish();
                            } else {
                                // 권한 요청
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_ACCESS_FINE_LOCATION);
                            }
                        }
                    });
                    // 다이얼로그 생성
                    AlertDialog alert = alert_confirm.create();
                    // 다이얼로그 보기
                    alert.show();

                    alert.setCanceledOnTouchOutside(false);
                    alert.setCancelable(false);


                } else {
                    //권한 받았음
                    startActivity(new Intent(getApplication(), MainPage.class));
                    MainActivity.this.finish();
                }
            } else {
                // 마쉬멜로우 미만 기기
                startActivity(new Intent(getApplication(), MainPage.class));
                MainActivity.this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }


    // 권한 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 확인
            startActivity(new Intent(getApplication(), MainPage.class));
            MainActivity.this.finish();
        } else {
            // 취소 > 재요청
            //Toast.makeText(context, "위치정보가 없을 경우 서울특별시를 기준으로 합니다.",
            //Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplication(), MainPage.class));
            finish();
        }
    }


}
