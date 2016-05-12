package com.sensei374121.amey.memoryappfinalproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

public class Adapter_SearchList extends FirebaseRecyclerAdapter<POJO_UserBIO, Adapter_SearchList.SearchListViewHolder> {

    //initial declarations
    private android.content.Context mContext ;
    static OnItemClickListener mItemClickListener;

    public Adapter_SearchList(Class<POJO_UserBIO> modelClass, int modelLayout, Class<SearchListViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext=context;
    }

    @Override
    protected void populateViewHolder(SearchListViewHolder searchListViewHolder, POJO_UserBIO memoryObj, int i) {
        searchListViewHolder.vMem_name.setText(memoryObj.getName());
        searchListViewHolder.vMem_email.setText(memoryObj.getEmail());
        Picasso.with(mContext).load(memoryObj.getProfilepic()).into(searchListViewHolder.vMem_Image);
    }

    public static class SearchListViewHolder extends RecyclerView.ViewHolder {
        public TextView vMem_name, vMem_email;
        public ImageView vMem_Image;
        public Button vButton;

        public SearchListViewHolder(View itemView) {
            super(itemView);

            vMem_name = (TextView) itemView.findViewById(R.id.RV_mem_name);
            vMem_Image = (ImageView) itemView.findViewById(R.id.RV_search_image);
            vMem_email = (TextView) itemView.findViewById(R.id.RV_mem_addr);
            vButton = (Button) itemView.findViewById(R.id.addfriend);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v,getPosition());
                }
            });

            vButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onAddFriendClicked(v,getPosition());
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onAddFriendClicked(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

}
