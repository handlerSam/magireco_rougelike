package com.live2d.rougelike;

import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MapCollectionAdapter extends RecyclerView.Adapter<MapCollectionAdapter.ViewHolder> {
    private ArrayList<Collection> collections = new ArrayList<>();
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView item_image;
        TextView item_name;
        TextView item_effect_description;

        public ViewHolder(View view){
            super(view);
            item_image = (ImageView) view.findViewById(R.id.item_image);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_effect_description = (TextView) view.findViewById(R.id.item_effect_description);
        }
    }

    public MapCollectionAdapter(Context context, ArrayList<Collection> itemList){
        //collections = itemList;
        collections.clear();
        for(Collection c : itemList){
            if(c.isOwn){
                collections.add(c);
            }
        }
        this.context = context;
        if(collections.size() == 0){
            ((MapActivity)context).no_item_text_view.setVisibility(View.VISIBLE);
        }else{
            ((MapActivity)context).no_item_text_view.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_collection_item, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Collection c = collections.get(position);
        holder.item_image.setImageResource(getImageByString(c.icon));
        holder.item_name.setText(c.name);
        holder.item_effect_description.setText(c.effectDescription);
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public int getImageByString(String name){
        Resources res = context.getResources();
        return res.getIdentifier(name,"drawable",context.getPackageName());
    }
}
