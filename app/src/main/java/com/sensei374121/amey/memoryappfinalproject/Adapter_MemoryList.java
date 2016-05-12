package com.sensei374121.amey.memoryappfinalproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

public class Adapter_MemoryList extends FirebaseRecyclerAdapter<POJO_MemoriesRVDisplay, Adapter_MemoryList.MemoryViewHolder> {

    //initial declarations
    private android.content.Context mContext ;
    static OnItemClickListener mItemClickListener;
    Query ref;

    public Adapter_MemoryList(Class<POJO_MemoriesRVDisplay> modelClass, int modelLayout, Class<MemoryViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext=context;
        this.ref= (Query) ref;
    }

    @Override
    protected void populateViewHolder(Adapter_MemoryList.MemoryViewHolder memoryViewHolder, POJO_MemoriesRVDisplay memoryObj, int i) {
        memoryViewHolder.vMem_name.setText(memoryObj.getMem_name());
        memoryViewHolder.vMem_addr.setText(memoryObj.getMem_place_street_address());
        Picasso.with(mContext).load((String) memoryObj.getImages().get(0)).into(memoryViewHolder.vMem_Image);
    }

    public static class MemoryViewHolder extends RecyclerView.ViewHolder {
        public TextView vMem_name, vMem_addr;
        public ImageView vMem_Image;

        public MemoryViewHolder(View itemView) {
            super(itemView);

            vMem_name = (TextView) itemView.findViewById(R.id.RV_mem_name);
            vMem_Image = (ImageView) itemView.findViewById(R.id.RV_mem_image);
            vMem_addr = (TextView) itemView.findViewById(R.id.RV_mem_addr);

            //see if you can inflate diferent layouts

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v,getPosition());
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

}
