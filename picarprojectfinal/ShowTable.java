package com.example.picarprojectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowTable extends AppCompatActivity {

    public ArrayList<PicarData> res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table);

        Bundle bundle = getIntent().getExtras();
        res = bundle.getParcelableArrayList("cars");

        init(res.size());

    }

    public void init(int s) {
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" ID ");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Time ");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Distance ");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" Message ");
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        for (int i = 0; i < s; i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText(""+(i+1));
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.LEFT);
            t1v.setHeight(80);
            t1v.setWidth(100);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(res.get(i).getTime());
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.LEFT);
            t2v.setHeight(80);
            t2v.setWidth(600);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("" + res.get(i).getDistance());
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.LEFT);
            t3v.setHeight(80);
            t3v.setWidth(300);
            tbrow.addView(t3v);
            TextView t4v = new TextView(this);
            t4v.setText("" + res.get(i).getID());
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.LEFT);
            tbrow.addView(t4v);


            stk.addView(tbrow);

        }

    }
}
