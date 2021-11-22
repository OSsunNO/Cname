package com.cname.nada.functions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cname.nada.R;

import java.util.ArrayList;

public class RecyclerViewAdapterInFrag2 extends RecyclerView.Adapter<RecyclerViewAdapterInFrag2.ViewHolder> {

    private ArrayList<ArrayList<String>> mData = null;
    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos, String userId);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile;
        TextView textViewName;
        TextView textViewBelong;
        TextView textViewPosition;
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

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageViewProfile = itemView.findViewById(R.id.profile_image);
            textViewName = itemView.findViewById(R.id.name);
            textViewBelong = itemView.findViewById(R.id.belong);
            textViewPosition = itemView.findViewById(R.id.position);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RecyclerViewAdapterInFrag2() { }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerViewAdapterInFrag2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        RecyclerViewAdapterInFrag2.ViewHolder vh = new RecyclerViewAdapterInFrag2.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecyclerViewAdapterInFrag2.ViewHolder holder, int position) {
        holder.userId = mData.get(position).get(0);
        String nameOfFriend = mData.get(position).get(1);
        String belongOfFriend = mData.get(position).get(2);
        String positionOfFriend = mData.get(position).get(3);
        holder.textViewName.setText(nameOfFriend);
        holder.textViewBelong.setText(belongOfFriend);
        holder.textViewPosition.setText(positionOfFriend);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // data 모델의 객체들을 list에 저장
    public void setList(ArrayList<ArrayList<String>> list) {
        this.mData = list;
        notifyDataSetChanged();
    }
}
