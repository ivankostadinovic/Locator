package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PopUpFeedQuest extends AppCompatActivity {

    private TextView txtName,txtDesc;
    private Button btnTake,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_feed_quest);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width=dm.widthPixels;
        int height=dm.heightPixels;


        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);
        getWindow().setLayout((int)(width*.8),(int)(height*.5)) ;

        txtName=findViewById(R.id.pop_quest_name);
        txtDesc=findViewById(R.id.pop_quest_description);
        btnTake=findViewById(R.id.btn_take);
        btnCancel=findViewById(R.id.btn_Cancel);


        Quest q=(Quest)getIntent().getSerializableExtra("Quest");
        txtName.setText(q.getName());
        txtDesc.setText(q.getDescription());

    }
}