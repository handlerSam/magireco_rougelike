package com.live2d.rougelike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ShopMemoriaAdapter extends RecyclerView.Adapter<ShopMemoriaAdapter.ViewHolder> {
    //直接把ViewHolder与xml文件适配
    private List<Memoria> mlist;
    Context context;
    ArrayList<ShopMemoriaAdapter.ViewHolder> holderList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        public ViewHolder(View view, CardView cardView){
            super(view);
            this.cardView = cardView;
        }
    }

    //构造方法
    public ShopMemoriaAdapter(List<Memoria> mList, Context context){
        this.mlist = mList;
        this.context = context;
    }


    //以下为三个必须重写的方法：
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = new CardView(context,parent);
        final ShopMemoriaAdapter.ViewHolder holder = new ShopMemoriaAdapter.ViewHolder(view.v, view);
        holderList.add(holder);
        view.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                final Memoria m = mlist.get(i);
                if(m.lvNow != m.lvMax){
                    final View dialog_layout = ((AdjustmentHouseActivity)context).getLayoutInflater().inflate(R.layout.memoria_lv_up_alert_dialog,null,false);
                    CardView left_memoria = dialog_layout.findViewById(R.id.left_memoria);
                    CardView right_memoria = dialog_layout.findViewById(R.id.right_memoria);
                    left_memoria.setMemoria(m.id);
                    left_memoria.setLv(m.lvNow,m.lvMax);
                    left_memoria.equippingView.setVisibility(View.INVISIBLE);
                    left_memoria.setUnableToEquip(false);

                    right_memoria.setMemoria(m.id);
                    m.setBreakthrough(4);
                    right_memoria.setLv(m.lvMax,m.lvMax);
                    m.setBreakthrough(0);
                    right_memoria.equippingView.setVisibility(View.INVISIBLE);
                    right_memoria.setUnableToEquip(false);

                    TextView skill_origin_description = dialog_layout.findViewById(R.id.skill_origin_description);
                    skill_origin_description.setText(m.getEffectDescription(false));
                    TextView skill_after_description = dialog_layout.findViewById(R.id.skill_after_description);
                    skill_after_description.setText(m.getEffectDescription(true));

                    TextView skill_origin_attr = dialog_layout.findViewById(R.id.skill_origin_attr);
                    skill_origin_attr.setText("HP:"+m.HPOrigin+" ATK:"+m.ATKOrigin+" DEF:"+m.DEFOrigin);
                    TextView skill_after_attr = dialog_layout.findViewById(R.id.skill_after_attr);
                    skill_after_attr.setText("HP:"+m.HPAfter+" ATK:"+m.ATKAfter+" DEF:"+m.DEFAfter);

                    final int price = StartActivity.MEMORIA_LV_UP_PRICE[m.star-2];
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    //dialog.setTitle("记忆升级: "+m.name);//标题
                    dialog.setCancelable(true);//是否能点击屏幕取消该弹窗
                    dialog.setView(dialog_layout);
                    if(price <= StartActivity.ccNumber){
                        dialog.setMessage("将花费 "+ price +" cc对记忆进行升级: ");//正文
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //正确逻辑
                                StartActivity.ccNumber -= price;
                                m.setBreakthrough(4);
                                m.setLv(m.lvMax);
                                ((AdjustmentHouseActivity)context).updateCCAndGriefSeedView();
                                ((AdjustmentHouseActivity)context).memoria_breakthrough.performClick();
                            }});
                    }else{
                        dialog.setMessage("需要花费 "+ price +" 个悲叹之种，悲叹之种不足.");//正文
                    }

                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //错误逻辑
                        }});
                    dialog.show();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//注意有个同名方法，不要覆写错了
        Memoria m = mlist.get(position);
        holder.cardView.setMemoria(m.id);
        holder.cardView.setLv(m.lvNow,m.lvMax);
        holder.cardView.setUnableTextView("Lv Max");
        holder.cardView.equippingView.setVisibility(View.INVISIBLE);
        holder.cardView.setUnableToEquip((m.lvNow == m.lvMax));
    }

    //该方法决定了ListView内控件的数量
    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
