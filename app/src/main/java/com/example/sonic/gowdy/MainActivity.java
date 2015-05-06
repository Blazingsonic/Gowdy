package com.example.sonic.gowdy;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.spinnerView) Spinner mSpinner;
    @InjectView(R.id.sortierenButton) Button mSortButton;
    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        ArrayList<Kneipe> kneipen = getListData();
        mRecyclerView.setHasFixedSize(true); // Not always recommended, but in this case enhances performance

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Kneipenadapter mAdapter = new Kneipenadapter(kneipen, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void addListenerOnSpinnerItemSelection() {
        mSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void addListenerOnButton() {
        mSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Filter the results
            }
        });
    }

    private ArrayList<Kneipe> getListData() {
        ArrayList<Kneipe> kneipen = new ArrayList<Kneipe>();

        Kneipe kneipe = new Kneipe();
        kneipe.setName("Restaurant Giovanni");
        kneipe.setAdresse("Birkenalle 2, Stuttgart");
        kneipe.setTyp("Italienische Speisen");
        kneipe.setBewertung("5");
        kneipen.add(kneipe);

        kneipe = new Kneipe();
        kneipe.setName("Schnell und Zackig Imbiss");
        kneipe.setAdresse("Schloss-Straße 54, Neukirch");
        kneipe.setTyp("Türkisches Fast Food");
        kneipe.setBewertung("3");
        kneipen.add(kneipe);

        kneipe = new Kneipe();
        kneipe.setName("Taurin Bierstube");
        kneipe.setAdresse("Henklegasse 13, Freiburg");
        kneipe.setTyp("Bier brauen");
        kneipe.setBewertung("4");
        kneipen.add(kneipe);

        kneipe = new Kneipe();
        kneipe.setName("Quietschfidel Cocktails");
        kneipe.setAdresse("Henklegasse 14, Ulm");
        kneipe.setTyp("Cocktails mixen");
        kneipe.setBewertung("5");
        kneipen.add(kneipe);

        kneipe = new Kneipe();
        kneipe.setName("Alte Veranda");
        kneipe.setAdresse("Bauernfüße 13, Emmendingen");
        kneipe.setTyp("Bier und Wein");
        kneipe.setBewertung("1");
        kneipen.add(kneipe);

        kneipe = new Kneipe();
        kneipe.setName("Taurin Bockwurst");
        kneipe.setAdresse("Ploppenhof 4, Furtwangen");
        kneipe.setTyp("Bier brauen");
        kneipe.setBewertung("3");
        kneipen.add(kneipe);

        kneipe = new Kneipe();
        kneipe.setName("Vietnamesisches Stüble");
        kneipe.setAdresse("Herzogenalle, Villingen");
        kneipe.setTyp("Vietnamesisch");
        kneipe.setBewertung("5");
        kneipen.add(kneipe);

        kneipe = new Kneipe();
        kneipe.setName("Flotter Otto");
        kneipe.setAdresse("Schwabengasse 14, Villingen");
        kneipe.setTyp("Schwäbische Küche");
        kneipe.setBewertung("3");
        kneipen.add(kneipe);

        kneipe = new Kneipe();
        kneipe.setName("Zum Bergbrau");
        kneipe.setAdresse("Kahnum 87, Stuttgart");
        kneipe.setTyp("Gute Weine");
        kneipe.setBewertung("4");
        kneipen.add(kneipe);

        return kneipen;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
