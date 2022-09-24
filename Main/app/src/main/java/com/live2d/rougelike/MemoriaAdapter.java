package com.live2d.rougelike;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemoriaAdapter extends RecyclerView.Adapter<MemoriaAdapter.ViewHolder> {
    //直接把ViewHolder与xml文件适配
    private List<Memoria> mlist;
    Context context;
    Memoria choosingMemoria = null;
    ArrayList<MemoriaAdapter.ViewHolder> holderList = new ArrayList<>();

    Global global;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        public ViewHolder(View view, CardView cardView){
            super(view);
            this.cardView = cardView;
        }
    }
    public MemoriaAdapter(List<Memoria> mList, Context context){
        this.mlist = mList;
        this.context = context;
        this.global = (Global)context.getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = new CardView(context,parent);
        final ViewHolder holder = new ViewHolder(view.v, view);
        holderList.add(holder);
        view.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = holder.getAdapterPosition();
                Memoria m = mlist.get(i);
                if(choosingMemoria != null){
                    if(choosingMemoria == m){
                        choosingMemoria.isChoosed = false;
                        choosingMemoria = null;
                        for(ViewHolder h: holderList){
                            h.cardView.setChoose(false);
                        }
                        MemoriaActivity mActivity = (MemoriaActivity)context;
                        mActivity.showingMemoriaView.setUnusable();
                        mActivity.HPShowingView.setText("");
                        mActivity.ATKShowingView.setText("");
                        mActivity.DEFShowingView.setText("");
                        mActivity.break_throughLinearLayout.setVisibility(View.INVISIBLE);
                        mActivity.coolTimeView.setVisibility(View.INVISIBLE);
                        mActivity.showingMemoriaNameView.setVisibility(View.INVISIBLE);
                        mActivity.skillIcon.setVisibility(View.INVISIBLE);
                        mActivity.skillDescriptionView.setText("");
                        mActivity.skillNameView.setVisibility(View.INVISIBLE);
                        mActivity.setView.setVisibility(View.INVISIBLE);
                        return;
                    }else{
                        choosingMemoria.isChoosed = false;
                        choosingMemoria = null;
                        for(ViewHolder h: holderList){
                            h.cardView.setChoose(false);
                        }
                    }
                }
                //choosingCard = holder.cardView;
                choosingMemoria = m;
                holder.cardView.setChoose(true);
                m.isChoosed = true;

                MemoriaActivity mActivity = (MemoriaActivity)context;
                mActivity.showingMemoriaView.setMemoria(m.id);
                mActivity.showingMemoriaView.setLv(m.lvNow,m.lvMax);
                mActivity.showingMemoriaView.setChoose(false);
                mActivity.showingMemoriaNameView.setText(m.name);
                mActivity.HPShowingView.setText(m.breakthrough == 4?  ""+ m.HPAfter :  "" +m.HPOrigin);
                mActivity.ATKShowingView.setText(m.breakthrough == 4? ""+ m.ATKAfter: "" +m.ATKOrigin);
                mActivity.DEFShowingView.setText(m.breakthrough == 4? ""+ m.DEFAfter: "" +m.DEFOrigin);
                mActivity.break_throughLinearLayout.setVisibility(m.breakthrough == 4? View.VISIBLE:View.INVISIBLE);
                if(m.isSkill()){
                    mActivity.coolTimeView.setVisibility(View.VISIBLE);
                    mActivity.coolTimeView.setText("冷却回合数 "+(m.breakthrough == 4? m.CDAfter:m.CDOrigin));
                }else{
                    mActivity.coolTimeView.setVisibility(View.INVISIBLE);
                }
                mActivity.showingMemoriaNameView.setVisibility(View.VISIBLE);
                mActivity.skillNameView.setVisibility(View.VISIBLE);
                //mActivity.skillNameView.setText();
                mActivity.skillIcon.setImageResource(getResourceByString(m.icon));
                mActivity.skillIcon.setVisibility(View.VISIBLE);
                mActivity.skillDescriptionView.setText(m.getEffectDescription());
                mActivity.setView.setImageResource(R.drawable.set);
                mActivity.setView.setVisibility((m.canSet && global.chooseEquipSlot != -1)? View.VISIBLE:View.INVISIBLE);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//注意有个同名方法，不要覆写错了
        //该方法在该View出现在屏幕上时被调用，次序晚于onCreate
        //获得position对应的类，并设置holder的内容
        Memoria m = mlist.get(position);
        holder.cardView.setMemoria(m.id);
        holder.cardView.setLv(m.lvNow,m.lvMax);
        holder.cardView.setChoose(m.isChoosed);
        if(m.characterId != -1){
            holder.cardView.equippingView.setVisibility(View.VISIBLE);
            holder.cardView.setEquippingResource(!(m.characterId == global.chooseCharacter));
        }else{
            holder.cardView.equippingView.setVisibility(View.INVISIBLE);
        }
        if(global.chooseEquipSlot != -1){
            boolean temp;
            if(m.isSkill()){
                temp = global.characterList.get(global.chooseCharacter).canEquipSkill(global.chooseEquipSlot);
            }else{
                temp = global.characterList.get(global.chooseCharacter).canEquipNormalcy(global.chooseEquipSlot);
            }
            holder.cardView.setUnableToEquip(!temp);
            m.canSet = temp;
        }
    }

    //该方法决定了ListView内控件的数量
    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public int getResourceByString(String idName){
        return context.getResources().getIdentifier(idName,"drawable", context.getPackageName());
    }
}
