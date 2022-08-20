package com.live2d.rougelike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class EffectDetailLayout extends ConstraintLayout {
    ConstraintLayout effectDetailLayout;
    TextView effectDetailName;
    TextView effectDetailCD;
    TextView effectDetailDescription;

    public EffectDetailLayout(@NonNull Context context, Effect e) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.effect_detail_layout,EffectDetailLayout.this);
        effectDetailLayout = findViewById(R.id.effectDetailLayout);
        effectDetailName = findViewById(R.id.effectDetailName);
        effectDetailCD = findViewById(R.id.effectDetailCD);
        effectDetailDescription = findViewById(R.id.effectDetailDescription);

        effectDetailName.setText(e.name);
        effectDetailCD.setText(e.getTime());
        effectDetailDescription.setText(e.getDescription());
    }
}