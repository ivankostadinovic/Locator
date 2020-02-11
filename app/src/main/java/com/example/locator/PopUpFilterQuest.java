package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PopUpFilterQuest extends AppCompatActivity {


    private Button btnConfirm, btnCancel;
    private EditText editRadius;
    private RadioButton radioIstorijski, radioZabavni, radioIstrazivacki, radioAll;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_feed_quest);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;


        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);
        getWindow().setLayout((int) (width * .8), LinearLayout.LayoutParams.WRAP_CONTENT);

        btnConfirm = findViewById(R.id.btn_take);
        btnCancel = findViewById(R.id.btn_Cancel);
        radioIstorijski = findViewById(R.id.radio_istorijski);
        radioZabavni = findViewById(R.id.radio_zabavni);
        radioIstrazivacki = findViewById(R.id.radio_istrazivacki);
        radioAll = findViewById(R.id.radio_all);
        radioGroup = findViewById(R.id.radio_group);

        editRadius = findViewById(R.id.radius);

        String filterType = getIntent().getStringExtra("type");
        Double filterRadius = getIntent().getDoubleExtra("radius", 0);

        editRadius.setText(filterRadius.toString());

        switch (filterType) {
            case Constants.QuestTypes.HISTORICAL:
                radioIstorijski.setChecked(true);
                break;
            case Constants.QuestTypes.SCIENTIFIC:
                radioIstrazivacki.setChecked(true);
                break;
            case Constants.QuestTypes.FUN:
                radioZabavni.setChecked(true);
                break;
            case Constants.QuestTypes.ALL:
                radioAll.setChecked(true);
                break;


        }


        btnConfirm.setOnClickListener(view -> {
            String type;
            Double radius = Double.parseDouble(editRadius.getText().toString());

            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_istorijski:
                    type = Constants.QuestTypes.HISTORICAL;
                    break;
                case R.id.radio_istrazivacki:
                    type = Constants.QuestTypes.SCIENTIFIC;
                    break;
                case R.id.radio_zabavni:
                    type = Constants.QuestTypes.FUN;
                    break;
                default:
                    type = Constants.QuestTypes.ALL;

            }
            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.putExtra("radius", radius);
            setResult(RESULT_OK, intent);
            finish();
        });
        findViewById(R.id.btn_Cancel).setOnClickListener(v->onBackPressed());
    }
}
