package com.example.sonic.gowdy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.sonic.gowdy.Kneipe;
import com.example.sonic.gowdy.R;
import com.example.sonic.gowdy.adapters.Kneipenadapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.spinnerView) Spinner mSpinner;
    @InjectView(R.id.spinnerView2) Spinner mSpinner2;
    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ArrayList<Kneipe> kneipen = getListData();
        mRecyclerView.setHasFixedSize(true); // Not always recommended, but in this case enhances performance

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final Kneipenadapter mAdapter = new Kneipenadapter(kneipen, this);
        mRecyclerView.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, String.format("value=%d", position));

                if (position != 0) {
                    loadItemsInSpinner2(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Filter
                ArrayList<Kneipe> kneipen = getListData();
                // New kneipen array
                ArrayList<Kneipe> kneipenFiltered = new ArrayList<Kneipe>();

                switch (position) {
                    // The filter code could be outsourced in a separate method
                    case 0:
                        kneipenFiltered = kneipen;
                        break;
                    case 1:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "5") {
                                Log.i(TAG, "This is 5 stars");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                    case 2:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "4") {
                                Log.i(TAG, "This is 4 stars");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                    case 3:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "3") {
                                Log.i(TAG, "This is 3 stars");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                    case 4:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "2") {
                                Log.i(TAG, "This is 2 stars");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                    case 5:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "1") {
                                Log.i(TAG, "This is 1 star");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                }

                mAdapter.setKneipen(kneipenFiltered);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadItemsInSpinner2(int position) {
        ArrayList<String> list = new ArrayList<String>();

        switch (position) {
            case 0:
                Log.i(TAG, "Auswählen");
                list.add("Zuerst Kategorie wählen");
                break;
            case 1:
                Log.i(TAG, "Sterne ausgewählt");
                list.add("Filter auswählen");
                list.add("5");
                list.add("4");
                list.add("3");
                list.add("2");
                list.add("1");
                break;
            case 2:
                Log.i(TAG, "Entfernung ausgewählt");
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner2.setAdapter(adapter);
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

    private ArrayList<Kneipe> getListData2() {
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

        return kneipen;
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
