package com.cname.nada;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cname.nada.functions.GpsTracker;
import com.cname.nada.functions.UserID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private ImageView returnBtn;
    String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/exchange/card/" + UserID.getUserId() + "/";
    private final String TAG = this.getClass().getSimpleName();

    //시간 받아오기
    private long getTime() {
        long realTime = 0;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String getTime = dateFormat.format(date);

        String[] timeArr = getTime.split(":");
        for (int i = 0; i<3; i++) {
            realTime += (long) Math.pow(60, 2-i) * Long.parseLong(timeArr[i]);
        }
        return realTime;
    }


    //위치 받아오기
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        returnBtn = findViewById(R.id.ReturnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }
        else{
            checkRunTimePermission();
        }


        long getTimeAmount = getTime();
        gpsTracker = new GpsTracker(getApplicationContext());
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        JSONObject parameter = new JSONObject();
        try {
            parameter.put("getTime", getTimeAmount);
            parameter.put("latitude", latitude);
            parameter.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url1, parameter,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast = Toast.makeText(getApplicationContext(), "유저 정보가 서버로 전송되었습니다.", Toast.LENGTH_LONG);
                        toast.show();

                        Log.d(TAG, "Put success : " + parameter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "유저 정보가 정상적으로 전송되지 않습니다.", Toast.LENGTH_LONG);
                        toast.show();

                        error.printStackTrace();
                        Log.d(TAG, "Put Fail");
                    }
                });

        queue.add(jsonObjectRequest);

//        이건 https://mine-it-record.tistory.com/191 보면서 하고 있음
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 시간 지난 후 실행할 코딩
//            }
//        }, 8000); // 8초 후

    }

    //ActivityCopat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드

    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            //요청코드가 PERMISSIONS_REQUEST_CODE이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;

            //모든 퍼미션을 허용했는지 체크

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                //위치 값을 가져올 수 있음
            } else {
                //거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유 설명 후 앱 종료 - 2가지 경우

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        //1. 위치 퍼미션을 가지고 있는지 체크
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){

            //2. 이미 퍼미션을 가지고 있다면
            //(안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 것으로 인식)

            //3. 위치 값 가져올 수 있음
        }
        else{ //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요. 2가지의 경우(3-1, 4-1)

            //3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){

                //3-2. 요청을 진행하기 전에 사용자가 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getApplicationContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                //3-3. 사용자에게 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
            else{
                //4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                //요청 결과는 똑같이 onRequestPermissionResult에서 수신
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress(double latitude, double longitude){
        //지오코더 - GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try{

            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        }
        catch(IOException ioException){
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        }
        catch(IllegalArgumentException illegalArgumentException){
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0){
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }

    //여기서부터 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"+"위치 설정을 수정하십시오.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성화했는지 검사
                if(checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 중.");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }
    public boolean checkLocationServicesStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
