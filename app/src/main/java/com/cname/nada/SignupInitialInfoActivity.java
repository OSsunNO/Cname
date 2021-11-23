package com.cname.nada;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cname.nada.functions.UserID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupInitialInfoActivity extends AppCompatActivity {

    private String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/user/login/addinfo/" + UserID.getUserId() + "/";
    private final String TAG = this.getClass().getSimpleName();
    Button nextButton;
    EditText nameEdit, phoneEdit, blueToothEdit, birthEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_initial_info);

        nameEdit = findViewById(R.id.nameEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        blueToothEdit = findViewById(R.id.blueToothEdit);
        birthEdit = findViewById(R.id.birthEdit);

        nameEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });
        phoneEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });
        blueToothEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });
        birthEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        phoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        nextButton = (Button) findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject parameter = new JSONObject();
                try {
                    parameter.put("name", nameEdit.getText());
                    parameter.put("phone_num", phoneEdit.getText());
                    parameter.put("bluetooth_data", blueToothEdit.getText());
                    parameter.put("birth_data", birthEdit.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(SignupInitialInfoActivity.this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url1, parameter,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast toast = Toast.makeText(getApplicationContext(), "유저 정보가 서버로 전송되었습니다.", Toast.LENGTH_LONG);
                                toast.show();

                                Intent intent = new Intent(SignupInitialInfoActivity.this, SignupInitialInfoActivity2.class);
                                startActivity(intent);
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
            }
        });
    }

    public static boolean isValidCellPhoneNumber(String cellphoneNumber) {

        boolean returnValue = false;
        try {
            String regex = "^\\s*(010|011|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(cellphoneNumber);
            if (m.matches()) {
                returnValue = true;
            }

            if (returnValue && cellphoneNumber != null
                    && cellphoneNumber.length() > 0
                    && cellphoneNumber.startsWith("010")) {
                cellphoneNumber = cellphoneNumber.replace("-", "");
                if (cellphoneNumber.length() != 11) {
                    returnValue = false;
                }
            }
            return returnValue;
        } catch (Exception e) {
            return false;
        }

    }
}