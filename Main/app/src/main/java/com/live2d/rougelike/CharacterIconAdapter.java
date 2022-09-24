package com.live2d.rougelike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CharacterIconAdapter extends RecyclerView.Adapter<CharacterIconAdapter.ViewHolder> {

    //直接把ViewHolder与xml文件适配
    ArrayList<Character> mPerson;//用于存储传入的List<Person>
    private Context context;
    Global global;

    //初始化ViewHolder，创建时直接绑定好两个布局
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView charImage;
        ConstraintLayout charFrame;
        ImageView charAttr;
        TextView lvView;
        ImageView retreat;
        LinearLayout starsLayout;
        ImageView[] char_star;
        public ViewHolder(View view){
            super(view);
            charImage = view.findViewById(R.id.charImage);
            charFrame = view.findViewById(R.id.charFrame);
            charAttr = view.findViewById(R.id.charAttr);
            lvView = view.findViewById(R.id.lvView);
            retreat = view.findViewById(R.id.retreat);
            starsLayout = view.findViewById(R.id.starsLayout);
            char_star = new ImageView[]{view.findViewById(R.id.char_star0),view.findViewById(R.id.char_star1),
                    view.findViewById(R.id.char_star2),view.findViewById(R.id.char_star3),view.findViewById(R.id.char_star4)};
        }
    }

    //构造方法
    public CharacterIconAdapter(Context context){
        this.context = context;
        global = (Global)context.getApplicationContext();
        mPerson = global.characterList;
    }

    //以下为三个必须重写的方法：
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_icon, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//注意有个同名方法，不要覆写错了
        //该方法在该View出现在屏幕上时被调用，次序晚于onCreate
        //获得position对应的类，并设置holder的内容
        final Character c = mPerson.get(position);
        holder.charImage.setImageResource(getResourceByString(c.charIconImage+"d"));
        holder.charImage.setBackgroundResource(getResourceByString("bg_"+c.element));
        holder.charFrame.setBackgroundResource(getResourceByString("frame_rank_"+c.star));
        holder.charAttr.setImageResource(getResourceByString(c.element));
        holder.lvView.setText("Lv "+c.lv);
        if(global.characters[global.choseCharacter] != null){
            if(global.characters[global.choseCharacter] == c){
                //说明该角色是当前选中的角色
                holder.retreat.setVisibility(View.VISIBLE);
                holder.starsLayout.setVisibility(View.INVISIBLE);
                holder.charImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TeamChooseActivity t = (TeamChooseActivity)context;
                        for(int i = 0; i < 5; i++){
                            if(global.characters[i] == c){
                                global.characters[i] = null;
                            }
                        }
                        if(c.isLeader){
                            c.isLeader = false;
                            for(int i = 0; i < 5; i++){
                                if(global.characters[i] != null){
                                    global.characters[i].isLeader = true;
                                    break;
                                }
                            }
                        }
                        t.chooseChar[global.choseCharacter].setVisibility(View.INVISIBLE);
                        global.choseCharacter = -1;
                        t.charRecyclerLayout.setVisibility(View.GONE);
                        t.updateCheckingViews();
                    }
                });
                return;
            }
        }
        holder.retreat.setVisibility(View.INVISIBLE);
        holder.starsLayout.setVisibility(View.VISIBLE);
        for(int i = 0; i < 5; i++){
            holder.char_star[i].setVisibility(i < c.star? View.VISIBLE:View.GONE);
        }

        holder.charImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamChooseActivity t = (TeamChooseActivity)context;
                for(int i = 0; i < 5; i++){
                    if(global.characters[i] == c){
                        c.isLeader = false;
                        global.characters[i] = null;
                    }
                }

                global.characters[global.choseCharacter] = c;

                boolean isTeamNoLeader = true;
                for(int i = 0; i < 5; i++){
                    if(global.characters[i] != null && global.characters[i].isLeader) {
                        isTeamNoLeader = false;
                        break;
                    }
                }
                if(isTeamNoLeader){
                    c.isLeader = true;
                }


                t.chooseChar[global.choseCharacter].setVisibility(View.INVISIBLE);
                global.choseCharacter = -1;
                t.charRecyclerLayout.setVisibility(View.GONE);
                t.updateCheckingViews();
            }
        });
    }

    //该方法决定了ListView内控件的数量
    @Override
    public int getItemCount() {
        return mPerson.size();
    }

    public int getResourceByString(String idName){
        return context.getResources().getIdentifier(idName,"drawable", context.getPackageName());
    }
}
