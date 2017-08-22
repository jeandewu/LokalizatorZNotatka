package com.example.rent.taskone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.Realm;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by RENT on 2017-08-22.
 */

public class LocationItemAdapter extends RecyclerView.Adapter<LocationItemAdapter.ItemViewHolder> {

    private final RealmResults<LocationItem> items;

    public LocationItemAdapter(RealmResults<LocationItem> items) {
        this.items = items;
    }

    @Override
    public LocationItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, null, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationItemAdapter.ItemViewHolder holder, int position) {
        LocationItem item = items.get(position);
        holder.noteLayout.setText(String.valueOf("Nr notatki:" + item.getId() + "Szerokosc geo.:" + item.getLatitude() +
                "Dlugosc geo.:" + item.getLonglitude()+"\n"+"Tresc notatki"+ item.getNote()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_layout)
        TextView noteLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
