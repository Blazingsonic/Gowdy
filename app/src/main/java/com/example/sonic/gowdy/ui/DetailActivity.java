package com.example.sonic.gowdy.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sonic.gowdy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DetailActivity extends Activity {

    @InjectView(R.id.nameLabel) TextView mName;
    @InjectView(R.id.adresseLabel) TextView mAdresse;
    @InjectView(R.id.typLabel) TextView mTyp;
    @InjectView(R.id.bewertungValue) TextView mBewertung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        // Get data from intent
        Intent intent = getIntent();
        mName.setText(intent.getStringExtra("Name"));
        mAdresse.setText(intent.getStringExtra("Adresse"));
        mTyp.setText(intent.getStringExtra("Typ"));
        mBewertung.setText(intent.getStringExtra("Bewertung"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
