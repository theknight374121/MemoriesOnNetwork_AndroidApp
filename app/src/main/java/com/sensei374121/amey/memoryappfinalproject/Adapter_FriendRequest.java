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

public class Adapter_FriendRequest extends FirebaseRecyclerAdapter<POJO_FriendRequests, Adapter_FriendRequest.FriendRequestViewHolder> {

    //initial declarations
    private Context mContext ;
    static OnItemClickListener mItemClickListener;

    public Adapter_FriendRequest(Class<POJO_FriendRequests> modelClass, int modelLayout, Class<FriendRequestViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext=context;
    }

    @Override
    protected void populateViewHolder(FriendRequestViewHolder friendRequestViewHolder, POJO_FriendRequests memoryObj, int i) {
        friendRequestViewHolder.vMem_name.setText(memoryObj.getName());
        friendRequestViewHolder.vMem_email.setText(memoryObj.getEmail());
        Picasso.with(mContext).load(memoryObj.getProfilepic()).into(friendRequestViewHolder.vMem_Image);
    }

    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        public TextView vMem_name, vMem_email;
        public ImageView vMem_Image;
        public Button vAcceptButton,vRejectButton;

        public FriendRequestViewHolder(View itemView) {
            super(itemView);

            vMem_name = (TextView) itemView.findViewById(R.id.RV_mem_name);
            vMem_Image = (ImageView) itemView.findViewById(R.id.RV_Frnd_image);
            vMem_email = (TextView) itemView.findViewById(R.id.RV_mem_email);
            vAcceptButton = (Button) itemView.findViewById(R.id.acceptButton);
            vRejectButton = (Button) itemView.findViewById(R.id.rejectButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v,getPosition());
                }
            });

            vAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onAcceptFriendClicked(v,getPosition());
                }
            });
            vRejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onRejectFriendClicked(v,getPosition());
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onAcceptFriendClicked(View view, int position);
        void onRejectFriendClicked(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

}
