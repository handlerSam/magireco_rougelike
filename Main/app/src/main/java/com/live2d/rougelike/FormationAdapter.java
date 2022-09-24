package com.live2d.rougelike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public  class FormationAdapter extends RecyclerView.Adapter<FormationAdapter.ViewHolder> {
    //直接把ViewHolder与xml文件适配
    private List<Formation> mFormation;//用于存储传入的List<Person>
    private ArrayList<FormationAdapter.ViewHolder> holderList = new ArrayList<>();
    Context context;
    Global global;

    //初始化ViewHolder，创建时直接绑定好两个布局
    static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout formationBackground;
        TextView name;
        ImageView[][] formationBlock = new ImageView[3][3];
        public ViewHolder(View view, Context context){
            super(view);
            formationBackground = view.findViewById(R.id.formationBackground);
            name = view.findViewById(R.id.name);
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    formationBlock[i][j] = view.findViewById(getIDByStr("formation0_"+i+"_"+j,context));
                }
            }
        }
    }

    //构造方法
    public FormationAdapter(Context context){
        this.context = context;
        this.mFormation = ((Global)context.getApplicationContext()).formationList;
        this.global = (Global)context.getApplicationContext();
    }

    //以下为三个必须重写的方法：
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.formation_item, parent,false);
        ViewHolder holder = new ViewHolder(view, context);
        holderList.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {//注意有个同名方法，不要覆写错了
        //该方法在该View出现在屏幕上时被调用，次序晚于onCreate
        //获得position对应的类，并设置holder的内容
        final Formation f = mFormation.get(position);
        final int temp = position;
        if(f.isChose){
            holder.formationBackground.setBackgroundResource(R.drawable.chose_formation_frame);
        }else{
            holder.formationBackground.setBackgroundResource(R.drawable.empty_formation_frame);
        }
        holder.name.setText(f.name);
        f.setFormation(holder.formationBlock,null,6);
        holder.formationBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!f.isChose){
                    ((FormationActivity)context).choseFormation.isChose = false;
                    ((FormationActivity)context).choseFormation = f;
                    global.usingFormationId = temp;
                    f.isChose = true;
                    ((FormationActivity)context).setBigFormation();
                    for(ViewHolder h: holderList){
                        if(h != holder){
                            h.formationBackground.setBackgroundResource(R.drawable.empty_formation_frame);
                        }
                    }
                    holder.formationBackground.setBackgroundResource(R.drawable.chose_formation_frame);
                }
            }
        });
    }

    //该方法决定了ListView内控件的数量
    @Override
    public int getItemCount() {
        return mFormation.size();
    }

    static public int getIDByStr(String idName, Context context){
        return context.getResources().getIdentifier(idName,"id", context.getPackageName());
    }
}
