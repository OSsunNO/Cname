package com.cname.nada.functions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckInternetState {

    public static void isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        Boolean connectResult = ni != null && ni.isConnected();
        if(connectResult==false) {
            Toast.makeText(context.getApplicationContext(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}

//https://bourbonkk.tistory.com/31 이거 참고해서 인터넷연결체크 함수 만듦.
