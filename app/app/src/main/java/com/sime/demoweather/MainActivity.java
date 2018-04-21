package com.sime.demoweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText Txt_Query;
    Button Btn_Search;
    EditText Txt_Current;
    EditText Txt_Min;
    EditText Txt_Max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Txt_Query = (EditText) this.findViewById(R.id.Txt_Query);
        Btn_Search = (Button) this.findViewById(R.id.Btn_Search);
        Txt_Current = (EditText) this.findViewById(R.id.Txt_Current);
        Txt_Min = (EditText) this.findViewById(R.id.Txt_Min);
        Txt_Max = (EditText) this.findViewById(R.id.Txt_Max);
    }
}
