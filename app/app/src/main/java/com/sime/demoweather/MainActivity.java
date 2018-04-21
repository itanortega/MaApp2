package com.sime.demoweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService queue = Executors.newSingleThreadExecutor();

    private final static String KEY = "96d1e8ee17719778cb471ad2f4c85ae2";
    private final static String DOMAIN = "https://api.openweathermap.org/data/2.5/weather";

    private EditText Txt_Query;
    private Button Btn_Search;
    private TextView Lbl_Current;
    private TextView Lbl_Min;
    private TextView Lbl_Max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Txt_Query = (EditText) this.findViewById(R.id.Txt_Query);
        Btn_Search = (Button) this.findViewById(R.id.Btn_Search);
        Lbl_Current = (TextView) this.findViewById(R.id.Lbl_Current);
        Lbl_Min = (TextView) this.findViewById(R.id.Lbl_Min);
        Lbl_Max = (TextView) this.findViewById(R.id.Lbl_Max);

        Btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = Txt_Query.getText().toString();
                search(query);
            }
        });
    }

    public void search(String query){
        final String queryTmp = query;

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                String strUrl = DOMAIN + "?q=" + queryTmp + "&appid=" + KEY;
                URL url = null;
                CAFData remoteData = null;

                try {
                    url = new URL(strUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if(url != null){
                    remoteData = CAFData.dataWithContentsOfURL(url);

                    try {
                        JSONObject root = new JSONObject(remoteData.toText());
                        JSONObject main = root.getJSONObject("main");
                        final float temp = (float) main.getDouble("temp");
                        final float temp_min = (float) main.getDouble("temp_min");
                        final float temp_max = (float) main.getDouble("temp_max");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Lbl_Current.setText(String.valueOf(temp));
                                Lbl_Min.setText(String.valueOf(temp_min));
                                Lbl_Max.setText(String.valueOf(temp_max));
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        queue.execute(thread);
    }
}
