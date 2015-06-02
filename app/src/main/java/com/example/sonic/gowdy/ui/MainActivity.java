package com.example.sonic.gowdy.ui;

import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.sonic.gowdy.Kneipe;
import com.example.sonic.gowdy.R;
import com.example.sonic.gowdy.adapters.Kneipenadapter;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private String mImageUrl = "http://webuser.hs-furtwangen.de/~taubew/android/image/schwarzwaldnebel1.jpg";
    private String mCouchUrl = "https://gowdy.iriscouch.com/gowdy/_design/gowdy/_view/alleKneipen";
    private String[] mTags = {"schwarzwaldnebel1.jpg", "schwarzwaldnebel2.jpg", "sonnenuntergang.jpg"};

    private ArrayList<Kneipe> mKneipen = null;
    private ArrayList<Kneipe> mKneipenFiltered = null;

    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.spinnerView) Spinner mSpinner;
    @InjectView(R.id.spinnerView2) Spinner mSpinner2;
    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.imageRequest) ImageView mImageRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // Couchdb

        // Get Image from request
        Picasso.with(this).load(mImageUrl).into(mImageRequest);

        setKneipenData();

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
                if (position != 0) {
                    updateKneipenData("spinner2", position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateKneipenData(String caller, int position) {
        mKneipenFiltered = new ArrayList<Kneipe>();

        switch (position) {
            // The filter code could be outsourced in a separate method
            case 0:
                mKneipenFiltered = mKneipen;
                break;
            case 1:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("5")) {
                        Log.i(TAG, "This is 5 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 2:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("4")) {
                        Log.i(TAG, "This is 4 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 3:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("3")) {
                        Log.i(TAG, "This is 3 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 4:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("2")) {
                        Log.i(TAG, "This is 2 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 5:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("1")) {
                        Log.i(TAG, "This is 1 star");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
        }

        updateDisplay(mKneipenFiltered);
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

    private void setKneipenData() {
        final ArrayList<Kneipe> kneipen = new ArrayList<Kneipe>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(mCouchUrl).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // Error
                Log.e(TAG, e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // Success
                String jsonData = response.body().string();
                try {
                    JSONObject jsonMain = new JSONObject(jsonData);
                    Log.v(TAG, jsonMain.toString());

                    JSONArray jsonRows = jsonMain.getJSONArray("rows");
                    Log.v(TAG, jsonRows.toString());

                    for (int i = 0; i < jsonRows.length(); i++) {
                        Log.v(TAG + " KneipenArray", jsonRows.get(i).toString());

                        JSONObject jsonKneipe = jsonRows.getJSONObject(i);

                        String value = jsonKneipe.getString("value");

                        JSONObject jsonValue = new JSONObject(value);
                        Log.v(TAG, jsonValue.toString());

                        String name = jsonValue.getString("name");
                        String adresse = jsonValue.getString("adresse");
                        String typ = jsonValue.getString("typ");
                        String bewertung = jsonValue.getString("bewertung");

                        Kneipe kneipe = makeKneipe(name, adresse, typ, bewertung);
                        Log.v(TAG, kneipe.toString());

                        kneipen.add(kneipe);
                        Log.v(TAG, kneipen.toString());

                        if (kneipen != null) {
                            updateDisplay(kneipen);
                        }

                        mKneipen = kneipen;
                        Log.v(TAG + " mKneipen", mKneipen.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateDisplay(final ArrayList<Kneipe> kneipen) {
        Log.i(TAG, kneipen.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setHasFixedSize(true); // Not always recommended, but in this case enhances performance

                mLayoutManager = new LinearLayoutManager(MainActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                final Kneipenadapter mAdapter = new Kneipenadapter(kneipen, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    private Kneipe makeKneipe(String name, String adresse, String typ, String bewertung) {
        Kneipe kneipe = new Kneipe();
        kneipe.setName(name);
        kneipe.setAdresse(adresse);
        kneipe.setTyp(typ);
        kneipe.setBewertung(bewertung);

        return kneipe;
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
