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

public class Adapter_Contacts extends FirebaseRecyclerAdapter<POJO_ContactsRVDisplay, Adapter_Contacts.ContactsViewHolder> {

    //initial declarations
    private Context mContext ;
    static OnItemClickListener mItemClickListener;

    public Adapter_Contacts(Class<POJO_ContactsRVDisplay> modelClass, int modelLayout, Class<ContactsViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext=context;
    }

    @Override
    protected void populateViewHolder(ContactsViewHolder contactsViewHolder, POJO_ContactsRVDisplay memoryObj, int i) {
        contactsViewHolder.vCon_name.setText(memoryObj.getName());
        contactsViewHolder.vCon_email.setText(memoryObj.getEmail());
        Picasso.with(mContext).load(memoryObj.getProfilepic()).into(contactsViewHolder.vCon_image);
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView vCon_name, vCon_email;
        public ImageView vCon_image;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            vCon_name = (TextView) itemView.findViewById(R.id.RV_con_name);
            vCon_image = (ImageView) itemView.findViewById(R.id.RV_con_image);
            vCon_email = (TextView) itemView.findViewById(R.id.RV_con_email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v,getPosition());
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

}
