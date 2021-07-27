package com.cname.nada.functions;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

public class ChangeStartFrag extends AppCompatActivity {

    public ChangeStartFrag() {
    }

    public void changeStartFrag(String name){
        InputFrag Inf = new InputFrag();
        Inf.setName(name);


        String filename = "jsons/start_frag_num.json";
        FileOutputStream outputStream;

        try {

            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(JsonUtil.toJSon(Inf).getBytes());
            outputStream.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}

class InputFrag {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

class JsonUtil {

    public static String toJSon(InputFrag inputFrag) {
        try {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("name", inputFrag.getName());

            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }
}
