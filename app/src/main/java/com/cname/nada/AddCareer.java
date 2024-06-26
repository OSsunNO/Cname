package com.cname.nada;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddCareer extends AppCompatActivity {

    ImageView returnBtn;
    Button saveButton2;
    EditText careerCategory, careerTitle, careerStartDate, careerFinDate, careerContent;
    private String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/career/upload/";
    private final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_career);

        returnBtn = (ImageView) findViewById(R.id.ReturnBtn);
        saveButton2 = (Button) findViewById(R.id.saveButton2);
        careerCategory =(EditText) findViewById(R.id.EditCCategory);
        careerTitle =(EditText) findViewById(R.id.EditCName);
        careerStartDate =(EditText) findViewById(R.id.EditCStart);
        careerFinDate =(EditText) findViewById(R.id.EditCFin);
        careerContent =(EditText) findViewById(R.id.EditCcontent);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        careerCategory.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });
        careerTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        careerStartDate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        careerFinDate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        careerContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });


        saveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(AddCareer.this);

                JSONObject parameter = new JSONObject();
                try{
                    parameter.put("user_id", UserID.getUserId());
                    parameter.put("career_category", careerCategory.getText());
                    parameter.put("career_title", careerTitle.getText());
                    parameter.put("career_start_date", careerStartDate.getText());
                    parameter.put("career_fin_date", careerFinDate.getText());
                    parameter.put("career_content", careerContent.getText());
                    parameter.put("career_picture", null);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, parameter,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent intent = new Intent(AddCareer.this, MainActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(AddCareer.this, "유저 정보가 정상적으로 전송되지 않습니다.", Toast.LENGTH_LONG);
                                toast.show();

                                error.printStackTrace();
                                Log.d(TAG, "Post Fail");
                            }
                        });
                queue.add(jsonObjectRequest);
            }
        });
    }
}