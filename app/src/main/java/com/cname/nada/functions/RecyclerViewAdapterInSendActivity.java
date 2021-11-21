package com.cname.nada.functions;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cname.nada.LoginActivity;
import com.cname.nada.R;
import com.cname.nada.SendActivity;
import com.cname.nada.SignupInitialInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RecyclerViewAdapterInSendActivity extends RecyclerView.Adapter<RecyclerViewAdapterInSendActivity.ViewHolder> {

    private ArrayList<ArrayList<String>> mData = null;
    private OnItemClickListener mListener = null;
    private String url1;
    private final String TAG = this.getClass().getSimpleName();

    public interface OnItemClickListener {
        void onItemClick(View v, int pos, String userId);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView addFriendButton;
        String userId;

        ViewHolder(View itemView) {
            super(itemView);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            mListener.onItemClick(v, pos, userId);
                        }
                    }
                }
            });

            itemView.findViewById(R.id.addFriendButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject params = new JSONObject() {
                    };
                    try {
                        params.put("id", UserID.getUserId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ImageView addFriendButton = itemView.findViewById(R.id.addFriendButton);

                    url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/add/friend/" + userId + "/";
                    RequestQueue queue = Volley.newRequestQueue(itemView.getContext());

                    try {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url1, params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        addFriendButton.setImageResource(R.drawable.already_add_friend);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast toast = Toast.makeText(itemView.getContext(), "유저 정보가 정상적으로 전송되지 않습니다.", Toast.LENGTH_LONG);
                                        toast.show();

                                        error.printStackTrace();
                                        Log.d(TAG, "Put Fail");
                                    }
                                });

                        queue.add(jsonObjectRequest);
                    }catch (Exception e){}
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            textViewName = itemView.findViewById(R.id.name);

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RecyclerViewAdapterInSendActivity(ArrayList<ArrayList<String>> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerViewAdapterInSendActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item_in_send_activity, parent, false);
        RecyclerViewAdapterInSendActivity.ViewHolder vh = new RecyclerViewAdapterInSendActivity.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecyclerViewAdapterInSendActivity.ViewHolder holder, int position) {
        holder.userId = mData.get(position).get(0);
        String nameOfFriend = mData.get(position).get(1);
        holder.textViewName.setText(nameOfFriend);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
}
